package io.quarkiverse.langfuse.runtime.otel;

import java.net.URI;
import java.time.Duration;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

import org.jboss.logging.Logger;

import io.opentelemetry.api.baggage.Baggage;
import io.opentelemetry.api.metrics.MeterProvider;
import io.opentelemetry.context.Context;
import io.opentelemetry.exporter.internal.http.HttpExporter;
import io.opentelemetry.exporter.internal.otlp.traces.TraceRequestMarshaler;
import io.opentelemetry.sdk.common.InternalTelemetryVersion;
import io.opentelemetry.sdk.internal.ComponentId;
import io.opentelemetry.sdk.internal.StandardComponentId;
import io.opentelemetry.sdk.trace.ReadWriteSpan;
import io.opentelemetry.sdk.trace.SpanProcessor;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import io.opentelemetry.semconv.incubating.GenAiIncubatingAttributes;
import io.quarkiverse.langfuse.config.LangfuseConfig;
import io.quarkiverse.langfuse.config.LangfuseOtelConfig.SpanFilterType;
import io.quarkus.opentelemetry.runtime.exporter.otlp.sender.VertxHttpSender;
import io.quarkus.opentelemetry.runtime.exporter.otlp.tracing.LateBoundSpanProcessor;
import io.quarkus.opentelemetry.runtime.exporter.otlp.tracing.VertxHttpSpanExporter;
import io.vertx.core.Vertx;

/**
 * Implementation of the {@link SpanProcessor} interface for integrating with Langfuse.
 * This processor acts as a wrapper around an underlying {@link SpanProcessor}
 * that is configured based on Langfuse-specific settings.
 *
 * The processor sends span data to the Langfuse server via the OTLP HTTP exporter.
 * The configuration for the server endpoint, authentication, and other runtime
 * settings are derived from a {@link LangfuseConfig} instance.
 *
 * The Langfuse processor supports filtering spans based on the provided
 * {@link SpanFilterType}. Depending on the filter, it may include all spans or
 * limit the scope to AI-related spans.
 */
public class LangfuseSpanProcessor extends LateBoundSpanProcessor {
    private static final Logger LOG = Logger.getLogger(LangfuseSpanProcessor.class);

    public LangfuseSpanProcessor(LangfuseConfig langfuseConfig, Vertx vertx) {
        super(BatchSpanProcessor.builder(createActualExporter(langfuseConfig, vertx)).build());
    }

    private static SpanExporter createActualExporter(LangfuseConfig langfuseConfig, Vertx vertx) {
        LOG.debug("Initializing Langfuse OTLP Span Processor");
        var exporter = createUnderlyingExporter(langfuseConfig, vertx);
        var filteredExporter = switch (langfuseConfig.otel().spanFilter()) {
            case ALL -> exporter;
            case AI_ONLY -> new FilteringAISpanExporter(exporter);
        };

        return new LangfuseAttributeEnrichingSpanExporter(filteredExporter, langfuseConfig);
    }

    private static SpanExporter createUnderlyingExporter(LangfuseConfig langfuseConfig, Vertx vertx) {
        var credentials = "%s:%s".formatted(langfuseConfig.publicKey(), langfuseConfig.secretKey());
        var authHeader = "Basic %s".formatted(Base64.getEncoder().encodeToString(credentials.getBytes()));
        var baseUri = URI.create(langfuseConfig.baseUrl());
        var signalPath = langfuseConfig.otel().traceIngestionPath();

        if (!signalPath.startsWith("/")) {
            signalPath = "/" + signalPath;
        }

        var sender = new VertxHttpSender(
                baseUri,
                signalPath,
                false,
                Duration.ofSeconds(10),
                Map.of("Authorization", authHeader, "x-langfuse-ingestion-version", "1"),
                "application/x-protobuf",
                options -> {
                },
                vertx);

        var httpExporter = new HttpExporter<TraceRequestMarshaler>(
                ComponentId.generateLazy(StandardComponentId.ExporterType.OTLP_HTTP_SPAN_EXPORTER),
                sender,
                MeterProvider::noop,
                InternalTelemetryVersion.LATEST,
                langfuseConfig.otel().traceIngestionUrl());

        return new VertxHttpSpanExporter(httpExporter);
    }

    @Override
    public void onStart(Context parentContext, ReadWriteSpan span) {
        Optional.ofNullable(
                Baggage.fromContext(parentContext).getEntryValue(GenAiIncubatingAttributes.GEN_AI_CONVERSATION_ID.getKey()))
                .ifPresent(
                        conversationId -> span.setAttribute(GenAiIncubatingAttributes.GEN_AI_CONVERSATION_ID, conversationId));

        super.onStart(parentContext, span);
    }

    @Override
    public boolean isStartRequired() {
        return true;
    }
}
