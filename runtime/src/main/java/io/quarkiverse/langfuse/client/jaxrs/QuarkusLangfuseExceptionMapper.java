package io.quarkiverse.langfuse.client.jaxrs;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status.Family;

import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

import io.quarkiverse.langfuse.client.LangfuseNotFoundException;
import io.quarkus.logging.Log;

public class QuarkusLangfuseExceptionMapper implements ResponseExceptionMapper<RuntimeException> {

    @Override
    public RuntimeException toThrowable(Response response) {
        var message = "Langfuse API error (%d): %s".formatted(response.getStatus(), response.readEntity(String.class));

        if (response.getStatus() == 404) {
            return new LangfuseNotFoundException(message);
        }

        if (response.getStatusInfo().getFamily() == Family.CLIENT_ERROR) {
            Log.warn(message);
            return null;
        }

        return new RuntimeException(message);
    }
}
