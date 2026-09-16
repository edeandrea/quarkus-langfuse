package io.quarkiverse.langfuse.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.Test;

class NamesTests {

    @Test
    void requireReturnsValueWhenNonBlank() {
        assertThat(Names.require("my-name", "Name")).isEqualTo("my-name");
    }

    @Test
    void requireThrowsWhenNullOrBlank() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Names.require(null, "Name"))
                .withMessage("Name must not be null or blank");

        assertThatIllegalArgumentException()
                .isThrownBy(() -> Names.require("   ", "Name"))
                .withMessage("Name must not be null or blank");
    }
}
