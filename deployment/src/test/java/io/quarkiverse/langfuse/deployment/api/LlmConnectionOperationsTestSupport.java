package io.quarkiverse.langfuse.deployment.api;

import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

/**
 * Serves a fake {@code /api/public/llm-connections} collection.
 */
abstract class LlmConnectionOperationsTestSupport extends PagedCollectionTestSupport {
    static final String LLM_CONNECTIONS_PATH = "/api/public/llm-connections";

    protected LlmConnectionOperationsTestSupport() {
        super(LLM_CONNECTIONS_PATH, ordinal -> """
                {
                  "id": "connection-%1$d",
                  "provider": "provider-%1$d",
                  "adapter": "openai",
                  "displaySecretKey": "sk-...redacted"
                }""".formatted(ordinal));
    }

    void stubConnections(int itemCount, int pageSize) {
        stubCollection(itemCount, pageSize);
    }

    void stubUpsert(String provider, String adapter) {
        wiremock().register(
                put(urlPathEqualTo(LLM_CONNECTIONS_PATH))
                        .willReturn(okJson("""
                                {
                                  "id": "connection-%s",
                                  "provider": "%s",
                                  "adapter": "%s",
                                  "displaySecretKey": "sk-...redacted"
                                }
                                """.formatted(provider, provider, adapter))));
    }

    void verifyUpsertRequests(int times) {
        wiremock().verifyThat(times, putRequestedFor(urlPathEqualTo(LLM_CONNECTIONS_PATH)));
    }
}
