package io.quarkiverse.langfuse.runtime.jackson;

import com.langfuse.api.model.ChatMessage;
import com.langfuse.api.model.ChatMessageWithPlaceholders;
import com.langfuse.api.model.PlaceholderMessage;

import io.quarkus.jackson.JacksonMixin;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.deser.std.StdDeserializer;

/**
 * Jackson 3 counterpart of {@code ChatMessageWithPlaceholdersJackson2Mixin}, used on Quarkus 4.x.
 *
 * <p>
 * The generated deserializer tries both {@link ChatMessage} and {@link PlaceholderMessage}
 * and expects exactly one match. With {@code FAIL_ON_UNKNOWN_PROPERTIES=false}, both always
 * succeed, causing a "2 classes match" error. This MixIn replaces the generated deserializer
 * with one that discriminates based on the {@code role} field (required in {@link ChatMessage}
 * but absent from {@link PlaceholderMessage}).
 */
@JacksonMixin(ChatMessageWithPlaceholders.class)
@JsonDeserialize(using = ChatMessageWithPlaceholdersJackson3Mixin.ChatMessageWithPlaceholdersDeserializer.class)
public interface ChatMessageWithPlaceholdersJackson3Mixin {

    class ChatMessageWithPlaceholdersDeserializer extends StdDeserializer<ChatMessageWithPlaceholders> {

        ChatMessageWithPlaceholdersDeserializer() {
            super(ChatMessageWithPlaceholders.class);
        }

        @Override
        public ChatMessageWithPlaceholders deserialize(JsonParser jp, DeserializationContext ctxt) throws JacksonException {
            var tree = ctxt.readTree(jp);

            return tree.has("role")
                    ? new ChatMessageWithPlaceholders(ctxt.readTreeAsValue(tree, ChatMessage.class))
                    : new ChatMessageWithPlaceholders(ctxt.readTreeAsValue(tree, PlaceholderMessage.class));
        }
    }
}
