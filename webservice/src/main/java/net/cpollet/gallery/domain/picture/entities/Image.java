package net.cpollet.gallery.domain.picture.entities;


import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;
import net.cpollet.gallery.domain.picture.values.Bytes;
import net.cpollet.gallery.domain.picture.values.Color;
import net.cpollet.gallery.domain.picture.values.Dimension;
import net.cpollet.gallery.domain.picture.values.Format;
import net.cpollet.gallery.domain.picture.values.ImageId;
import net.cpollet.gallery.domain.picture.values.Role;

import java.util.Optional;

@Value
@EqualsAndHashCode(of = "imageId")
public class Image {
    ImageId imageId;
    @NonNull Role role;
    @NonNull Bytes data;
    @NonNull Format format;
    @NonNull Dimension dimension;
    @NonNull Color backgroundColor;

    public Image(
            ImageId imageId,
            @NonNull Role role,
            @NonNull Bytes data,
            @NonNull Format format,
            @NonNull Dimension dimension,
            @NonNull Color backgroundColor
    ) {
        this.imageId = imageId;
        this.role = role;
        this.data = data;
        this.format = format;
        this.dimension = dimension;
        this.backgroundColor = backgroundColor;
    }

    public Image(
            @NonNull Role role,
            @NonNull Bytes data,
            @NonNull Format format,
            @NonNull Dimension dimension,
            @NonNull Color backgroundColor
    ) {
        this(null, role, data, format, dimension, backgroundColor);
    }

    public Optional<ImageId> getImageId() {
        return Optional.ofNullable(imageId);
    }
}
