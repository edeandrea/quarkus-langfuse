package io.quarkiverse.langfuse.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.Test;

class ValidationUtilsTests {

    @Test
    void ensureNotNullReturnsValueWhenNotNull() {
        assertThat(ValidationUtils.ensureNotNull("test", "arg")).isEqualTo("test");
    }

    @Test
    void ensureNotNullThrowsWhenNull() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> ValidationUtils.ensureNotNull(null, "request"))
                .withMessage("request must not be null");
    }

    @Test
    void ensureNotBlankReturnsValueWhenNonBlank() {
        assertThat(ValidationUtils.ensureNotBlank("my-name", "Name")).isEqualTo("my-name");
    }

    @Test
    void ensureNotBlankThrowsWhenNullOrBlank() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> ValidationUtils.ensureNotBlank(null, "Name"))
                .withMessage("Name must not be null or blank");

        assertThatIllegalArgumentException()
                .isThrownBy(() -> ValidationUtils.ensureNotBlank("   ", "Name"))
                .withMessage("Name must not be null or blank");
    }
}
