package net.cpollet.gallery.infrastructure.web.rest;

import lombok.NonNull;
import net.cpollet.gallery.domain.picture.values.Bytes;
import net.cpollet.gallery.domain.picture.values.Format;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

public enum Encoding {
    RAW(Bytes::getBytes, f->MediaType.parseMediaType(f.mimeType())),
    BASE64(Bytes::toBase64, f->MediaType.TEXT_PLAIN);

    private final Function<Bytes, Object> encoder;
    private final Function<Format, MediaType> contentTypeBuilder;

    Encoding(Function<Bytes, Object> encoder, Function<Format, MediaType> contentTypeBuilder) {
        this.encoder = encoder;
        this.contentTypeBuilder = contentTypeBuilder;
    }

    public static Optional<Encoding> from(@NonNull String encoding) {
        return Arrays.stream(Encoding.values())
                .filter(e -> e.name().equals(encoding.toUpperCase()))
                .findFirst();
    }

    public Object encode(@NonNull Bytes bytes) {
        return encoder.apply(bytes);
    }

    public MediaType contentType(Format format) {
        return contentTypeBuilder.apply(format);
    }
}
