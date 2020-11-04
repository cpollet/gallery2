package net.cpollet.gallery.domain.picture.values;

import lombok.NonNull;

import java.util.Arrays;
import java.util.Optional;

public enum Format {
    JPEG("image/jpeg");

    private final String mimeType;

    Format(String mimeType) {
        this.mimeType = mimeType;
    }

    public static Optional<Format> from(@NonNull String name) {
        return Arrays.stream(Format.values())
                .filter(f -> f.name().equals(name.toUpperCase()))
                .findFirst();
    }

    public String mimeType() {
        return mimeType;
    }
}
