package io.quarkiverse.langfuse.util;

/**
 * Utility class for validating method arguments.
 *
 * <p>
 * Inspired by
 * <a href=
 * "https://github.com/langchain4j/langchain4j/blob/main/langchain4j-core/src/main/java/dev/langchain4j/internal/ValidationUtils.java">
 * LangChain4j's ValidationUtils</a>.
 */
public final class ValidationUtils {

    private ValidationUtils() {
    }

    /**
     * Ensures that the given object is not null.
     *
     * @param object the object to check
     * @param name the name of the object to be used in the exception message
     * @param <T> the type of the object
     * @return the object if it is not null
     * @throws IllegalArgumentException if {@code object} is {@code null}
     */
    public static <T> T ensureNotNull(T object, String name) {
        if (object == null) {
            throw new IllegalArgumentException("%s must not be null".formatted(name));
        }

        return object;
    }

    /**
     * Ensures that the given string is not null and not blank.
     *
     * @param string the string to check
     * @param name the name of the string to be used in the exception message
     * @return the string if it is not null and not blank
     * @throws IllegalArgumentException if {@code string} is {@code null} or blank
     */
    public static String ensureNotBlank(String string, String name) {
        if ((string == null) || string.isBlank()) {
            throw new IllegalArgumentException("%s must not be null or blank".formatted(name));
        }

        return string;
    }
}
