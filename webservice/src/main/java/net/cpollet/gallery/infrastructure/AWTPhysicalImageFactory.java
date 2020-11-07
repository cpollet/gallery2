package net.cpollet.gallery.infrastructure;

import io.vavr.control.Either;
import net.cpollet.gallery.domain.picture.PhysicalImage;
import net.cpollet.gallery.domain.picture.entities.Image;
import net.cpollet.gallery.domain.picture.errors.DomainError;
import net.cpollet.gallery.domain.picture.errors.FormatNotSupported;
import net.cpollet.gallery.domain.picture.values.Bytes;
import net.cpollet.gallery.domain.picture.values.Dimension;
import net.cpollet.gallery.domain.picture.values.Format;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.function.Function;

public class AWTPhysicalImageFactory implements PhysicalImageFactory {
    @Override
    public Either<DomainError, PhysicalImage> create(Image image) {
        return create(image.getData());
    }

    @Override
    public Either<DomainError, PhysicalImage> create(Bytes bytes) {
        try {
            return createThrowingExceptions(bytes);
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Either<DomainError, PhysicalImage> createThrowingExceptions(Bytes bytes) throws IOException {
        ImageInputStream imageInputStream = ImageIO.createImageInputStream(bytes.getInputStream());
        Iterator<ImageReader> readers = ImageIO.getImageReaders(imageInputStream);

        if (readers.hasNext()) {
            ImageReader reader = readers.next();
            reader.setInput(imageInputStream);
            String formatName = reader.getFormatName();
            return Format
                    .from(formatName)
                    .map(format -> buildAWTPhysicalImage(bytes, reader, format))
                    .map((Function<PhysicalImage, Either<DomainError, PhysicalImage>>) Either::right)
                    .orElseGet(() -> Either.left(new FormatNotSupported(formatName)));
        }

        return Either.left(new FormatNotSupported());
    }

    private PhysicalImage buildAWTPhysicalImage(Bytes bytes, ImageReader reader, Format format) {
        try {
            return buildAWTPhysicalImageThrowingExceptions(bytes, reader, format);
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private AWTPhysicalImage buildAWTPhysicalImageThrowingExceptions(Bytes bytes, ImageReader reader, Format format)
            throws IOException {
        return new AWTPhysicalImage(
                new Dimension(reader.getWidth(0), reader.getHeight(0)),
                format,
                reader.read(0),
                bytes
        );
    }
}
