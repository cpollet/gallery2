package net.cpollet.gallery.domain.picture.values;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


class DimensionTest {
    @Test
    void getRatio() {
        Assertions.assertThat(new Dimension(800, 600).getRatio())
                .isEqualTo(4f / 3);
    }
}