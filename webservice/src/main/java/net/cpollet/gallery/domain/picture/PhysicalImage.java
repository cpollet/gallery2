package net.cpollet.gallery.domain.picture;

import net.cpollet.gallery.domain.picture.values.Bytes;
import net.cpollet.gallery.domain.picture.values.Dimension;
import net.cpollet.gallery.domain.picture.values.Format;

public interface PhysicalImage {
    Dimension getDimension();

    Format getFormat();

    Bytes getBytes();

    PhysicalImage resize(Dimension newDimension);
}
