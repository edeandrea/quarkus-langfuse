package io.quarkiverse.langfuse.client;

import com.langfuse.api.LangfuseApiException;

public class LangfuseNotFoundException extends LangfuseApiException {
    public LangfuseNotFoundException(String message) {
        super(message, 404);
    }
}
