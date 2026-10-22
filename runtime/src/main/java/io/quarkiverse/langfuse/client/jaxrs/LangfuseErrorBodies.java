package io.quarkiverse.langfuse.client.jaxrs;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import jakarta.ws.rs.core.MediaType;

import com.langfuse.api.LangfuseErrorBody;

import io.vertx.core.json.JsonObject;

/**
 * Turns a raw error response body into a {@link LangfuseErrorBody}.
 *
 * <p>
 * The {@code Content-Type} decides the strategy <em>before</em> anything is parsed. It is the only
 * discriminator that is disjoint by construction and lives outside the body, so a proxy's HTML error
 * page is classified as opaque without a speculative parse-and-catch. Every field-based
 * discriminator was rejected: Langfuse's error shapes are nested rather than disjoint and most
 * endpoints declare no error schema at all.
 *
 * <p>
 * This runs inside an exception mapper, so it must be total: a throw here would replace a clean 404
 * with an unrelated stack trace originating in the mapper itself. Every unparseable, empty, or
 * unexpected body therefore degrades to {@code opaque}/{@code empty}.
 */
final class LangfuseErrorBodies {

    private LangfuseErrorBodies() {
    }

    static LangfuseErrorBody parse(MediaType mediaType, String rawBody) {
        return Optional.ofNullable(rawBody)
                .filter(Predicate.not(String::isBlank))
                .map(body -> isJson(mediaType) ? parseJson(body) : LangfuseErrorBody.opaque(body))
                .orElseGet(LangfuseErrorBody::empty);
    }

    /**
     * A {@code null} media type - a response may carry none - answers {@code false}, routing the body
     * to the opaque path.
     *
     * <p>
     * {@code isCompatible} demands equal subtypes, so it covers {@code application/json} and the
     * wildcards but not the structured-suffix types such as {@code application/problem+json}; those
     * are matched separately on the {@code +json} suffix.
     */
    private static boolean isJson(MediaType mediaType) {
        return MediaType.APPLICATION_JSON_TYPE.isCompatible(mediaType)
                || Optional.ofNullable(mediaType)
                        .map(MediaType::getSubtype)
                        .map(subtype -> subtype.toLowerCase(Locale.ROOT))
                        .filter(subtype -> subtype.endsWith("+json"))
                        .isPresent();
    }

    /**
     * Parsed with Vert.x rather than Jackson: Quarkus 3.x ships Jackson 2 and Quarkus 4.x ships Jackson 3, and a
     * class compiled against one cannot be loaded against the other, whereas {@link JsonObject} is the same type
     * on both.
     */
    private static LangfuseErrorBody parseJson(String raw) {
        return parseObject(raw)
                .map(fields -> fromObject(fields, raw))
                .orElseGet(() -> LangfuseErrorBody.opaque(raw));
    }

    /**
     * @return the top-level scalar fields of {@code raw} as text, or empty if it is not a well-formed JSON object
     */
    private static Optional<Map<String, String>> parseObject(String raw) {
        try {
            return Optional.of(new JsonObject(raw))
                    .map(LangfuseErrorBodies::scalarFields);
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    private static Map<String, String> scalarFields(JsonObject root) {
        return root.getMap()
                .entrySet()
                .stream()
                .filter(e -> (e.getValue() instanceof String) || (e.getValue() instanceof Number)
                        || (e.getValue() instanceof Boolean))
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, e -> String.valueOf(e.getValue())));
    }

    /**
     * Langfuse names the machine-readable field {@code code} on the endpoints that declare a
     * {@code PublicApiError} and {@code error} elsewhere - {@code {"error":"LangfuseNotFoundError"}} -
     * so both names are consulted, in that order.
     */
    private static LangfuseErrorBody fromObject(Map<String, String> fields, String raw) {
        return LangfuseErrorBody.of(
                textField(fields, "message").orElse(raw),
                textField(fields, "code")
                        .or(() -> textField(fields, "error"))
                        .orElse(null),
                raw);
    }

    private static Optional<String> textField(Map<String, String> fields, String name) {
        return Optional.ofNullable(fields.get(name))
                .filter(Predicate.not(String::isBlank));
    }
}
