package net.cpollet.gallery.infrastructure;

import io.vavr.control.Either;
import net.cpollet.gallery.domain.picture.PhysicalImage;
import net.cpollet.gallery.domain.picture.entities.Image;
import net.cpollet.gallery.domain.picture.errors.DomainError;
import net.cpollet.gallery.domain.picture.values.Bytes;
import net.cpollet.gallery.domain.picture.values.Dimension;
import net.cpollet.gallery.domain.picture.values.Format;
import net.cpollet.gallery.domain.picture.values.Role;
import org.assertj.core.api.Assertions;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

class AWTPhysicalImageFactoryTest {
    @Test
    void createFromBytes() throws IOException {
        byte[] bytes = Objects.requireNonNull(
                AWTPhysicalImageFactoryTest.class.getClassLoader().getResourceAsStream("images/lena.jpg")
        ).readAllBytes();
        Either<DomainError, PhysicalImage> image = new AWTPhysicalImageFactory().create(new Bytes(bytes));

        VavrAssertions.assertThat(image).isRight();

        Assertions.assertThat(image.get()).extracting(PhysicalImage::getDimension).isEqualTo(new Dimension(400, 400));
        Assertions.assertThat(image.get()).extracting(PhysicalImage::getFormat).isEqualTo(Format.JPEG);
        Assertions.assertThat(image.get())
                .extracting(PhysicalImage::getBytes)
                .extracting(Bytes::getBytes)
                .isEqualTo(bytes);
    }

    @Test
    void createFromBytesThrowingIOException() {
        Bytes bytes = Mockito.mock(Bytes.class);
        InputStream jpegInputStreamThrowingIOExceptions = new InputStream() {
            final int[] data = new int[]{0xFF, 0xD8};
            int cursor = 0;

            @Override
            public int read() throws IOException {
                if (cursor > 1) {
                    throw new IOException();
                }
                return data[cursor++];
            }
        };

        Mockito.when(bytes.getInputStream()).thenReturn(jpegInputStreamThrowingIOExceptions);

        Either<DomainError, PhysicalImage> image = new AWTPhysicalImageFactory().create(bytes);

        VavrAssertions.assertThat(image).isLeft();
    }

    @Test
    void createFromBytesUnsupportedFormat() throws IOException {
        Bytes bytes = Mockito.mock(Bytes.class);
        InputStream inputStream = Mockito.mock(InputStream.class);
        Mockito.when(inputStream.read()).thenThrow(new IOException());
        Mockito.when(inputStream.read(ArgumentMatchers.any(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt()))
                .thenThrow(new IOException());
        Mockito.when(bytes.getInputStream()).thenReturn(inputStream);

        Either<DomainError, PhysicalImage> image = new AWTPhysicalImageFactory().create(bytes);

        VavrAssertions.assertThat(image).isLeft();
    }

    @Test
    void testFromImage() throws IOException {
        Either<DomainError, PhysicalImage> image = new AWTPhysicalImageFactory().create(new Image(
                Role.MAIN,
                new Bytes(
                        Objects.requireNonNull(
                                AWTPhysicalImageFactoryTest.class.getClassLoader().getResourceAsStream("images/lena.jpg")
                        ).readAllBytes()
                ),
                Format.JPEG,
                new Dimension(1, 2)
        ));

        VavrAssertions.assertThat(image).isRight();
        Assertions.assertThat(image.get()).extracting(PhysicalImage::getDimension).isEqualTo(new Dimension(400, 400));
        Assertions.assertThat(image.get()).extracting(PhysicalImage::getFormat).isEqualTo(Format.JPEG);
    }
}