package io.quarkiverse.langfuse.runtime.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.langfuse.api.model.ChatMessage;
import com.langfuse.api.model.ChatMessageWithPlaceholders;
import com.langfuse.api.model.PlaceholderMessage;

import io.quarkus.jackson.JacksonMixin;

/**
 * Fixes polymorphic deserialization of {@link ChatMessageWithPlaceholders}.
 *
 * <p>
 * The generated deserializer tries both {@link ChatMessage} and {@link PlaceholderMessage}
 * and expects exactly one match. With {@code FAIL_ON_UNKNOWN_PROPERTIES=false}, both always
 * succeed, causing a "2 classes match" error. This MixIn replaces the generated deserializer
 * with one that discriminates based on the {@code role} field (required in {@link ChatMessage}
 * but absent from {@link PlaceholderMessage}).
 */
@JacksonMixin(ChatMessageWithPlaceholders.class)
@JsonDeserialize(using = ChatMessageWithPlaceholdersMixin.ChatMessageWithPlaceholdersDeserializer.class)
public interface ChatMessageWithPlaceholdersMixin {

    class ChatMessageWithPlaceholdersDeserializer extends StdDeserializer<ChatMessageWithPlaceholders> {

        ChatMessageWithPlaceholdersDeserializer() {
            super(ChatMessageWithPlaceholders.class);
        }

        @Override
        public ChatMessageWithPlaceholders deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            JsonNode tree = ctxt.readTree(jp);

            if (tree.has("role")) {
                var chatMessage = tree.traverse(jp.getCodec()).readValueAs(ChatMessage.class);
                return new ChatMessageWithPlaceholders(chatMessage);
            }

            var placeholderMessage = tree.traverse(jp.getCodec()).readValueAs(PlaceholderMessage.class);
            return new ChatMessageWithPlaceholders(placeholderMessage);
        }
    }
}
