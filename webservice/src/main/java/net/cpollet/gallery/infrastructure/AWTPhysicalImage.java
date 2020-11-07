package net.cpollet.gallery.infrastructure;

import net.cpollet.gallery.domain.picture.PhysicalImage;
import net.cpollet.gallery.domain.picture.values.Bytes;
import net.cpollet.gallery.domain.picture.values.Dimension;
import net.cpollet.gallery.domain.picture.values.Format;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;


public class AWTPhysicalImage implements PhysicalImage {
    private final Dimension dimension;
    private final Format format;
    private final BufferedImage image;
    private final Bytes bytes;

    public AWTPhysicalImage(Dimension dimension, Format format, BufferedImage image, Bytes bytes) {
        this.dimension = dimension;
        this.format = format;
        this.image = image;
        this.bytes = bytes;
    }

    @Override
    public Dimension getDimension() {
        return dimension;
    }

    @Override
    public Format getFormat() {
        return format;
    }

    @Override
    public Bytes getBytes() {
        return bytes;
    }

    @Override
    public PhysicalImage resize(Dimension newDimension) {
        double xFactor = (double) newDimension.getWidth() / this.dimension.getWidth();
        double yFactor = (double) newDimension.getHeight() / this.dimension.getHeight();
        double scaleFactor = Math.min(xFactor, yFactor);

        Dimension innerDimension = new Dimension(
                ((int) Math.floor(scaleFactor * dimension.getWidth())),
                ((int) Math.floor(scaleFactor * dimension.getHeight()))
        );

        Image scaledImage = image.getScaledInstance(
                innerDimension.getWidth(),
                innerDimension.getHeight(),
                Image.SCALE_SMOOTH
        );

        BufferedImage newImage = new BufferedImage(newDimension.getWidth(), newDimension.getHeight(), image.getType());

        Graphics2D g2d = newImage.createGraphics();
        g2d.setBackground(Color.BLACK);
        g2d.drawImage(
                scaledImage,
                Math.floorDiv(newDimension.getWidth() - innerDimension.getWidth(), 2),
                Math.floorDiv(newDimension.getHeight() - innerDimension.getHeight(), 2),
                null
        );
        g2d.dispose();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(newImage, format.name(), out);
            return new AWTPhysicalImage(
                    newDimension,
                    format,
                    newImage,
                    new Bytes(out.toByteArray())
            );
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
