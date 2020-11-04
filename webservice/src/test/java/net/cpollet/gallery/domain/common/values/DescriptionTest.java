package net.cpollet.gallery.domain.common.values;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class DescriptionTest {
    @Test
    void noTags() {
        Assertions.assertThat(new Description("some description").tags())
                .isEmpty();
    }

    @Test
    void beginsWithTag() {
        Assertions.assertThat(new Description("#tag and some description").tags())
                .containsExactly(new Tag("tag", 0, 4));
    }

    @Test
    void endsWithTag() {
        Assertions.assertThat(new Description("some description and a #tag").tags())
                .containsExactly(new Tag("tag", 23, 27));
    }

    @Test
    void severalTags() {
        Assertions.assertThat(new Description("some #description and a #tag").tags())
                .containsExactly(new Tag("description", 5, 17), new Tag("tag", 24, 28));
    }
}