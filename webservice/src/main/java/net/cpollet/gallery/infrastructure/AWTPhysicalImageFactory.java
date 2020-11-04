package net.cpollet.gallery.infrastructure;

import io.vavr.control.Either;
import net.cpollet.gallery.domain.picture.PhysicalImage;
import net.cpollet.gallery.domain.picture.entities.Image;
import net.cpollet.gallery.domain.picture.errors.DomainError;
import net.cpollet.gallery.domain.picture.errors.FormatNotSupported;
import net.cpollet.gallery.domain.picture.errors.UnexpectedError;
import net.cpollet.gallery.domain.picture.values.Bytes;
import net.cpollet.gallery.domain.picture.values.Dimension;
import net.cpollet.gallery.domain.picture.values.Format;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.IOException;
import java.util.Iterator;

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
            return Either.left(new UnexpectedError());
        }
    }

    private Either<DomainError, PhysicalImage> createThrowingExceptions(Bytes bytes) throws IOException {
        ImageInputStream imageInputStream = ImageIO.createImageInputStream(bytes.getInputStream());
        Iterator<ImageReader> readers = ImageIO.getImageReaders(imageInputStream);

        if (readers.hasNext()) {
            ImageReader reader = readers.next();
            reader.setInput(imageInputStream);

            return Format
                    .from(reader.getFormatName())
                    .map(format -> getValue(bytes, reader, format))
                    .orElseGet(() -> Either.left(new FormatNotSupported()));
        }

        return Either.left(new FormatNotSupported());
    }

    private Either<DomainError, PhysicalImage> getValue(Bytes bytes, ImageReader reader, Format format) {
        try {
            return Either.right(new AWTPhysicalImage(
                    new Dimension(reader.getWidth(0), reader.getHeight(0)),
                    format,
                    reader.read(0),
                    bytes
            ));

        }
        catch (IOException e) {
            return Either.left(new UnexpectedError());
        }
    }
}
