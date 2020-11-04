package net.cpollet.gallery.domain.picture.values;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

class BytesTest {
    @Test
    void testToString() {
        Assertions.assertThat(new Bytes(new byte[]{65}).toString())
                .isEqualTo("[1 bytes data]");
    }

    @Test
    void base64() {
        Assertions.assertThat(new Bytes("Data".getBytes(StandardCharsets.UTF_8)).toBase64())
                .isEqualTo("RGF0YQ==");
    }

    @Test
    void hex() {
        Assertions.assertThat(new Bytes("Data".getBytes(StandardCharsets.UTF_8)).toHex())
                .isEqualTo("44617461");
    }

    @Test
    void size() {
        Assertions.assertThat(new Bytes("Data".getBytes(StandardCharsets.UTF_8)).size())
                .isEqualTo(4);
    }

    @Test
    void hash() {
        Assertions.assertThat(new Bytes("Data".getBytes(StandardCharsets.UTF_8)).hash().toHex())
                .isEqualTo("09b3a79b25e34167f695ae162d6741115cfd10c66af85fbf0df2e0798b939497");
    }
}