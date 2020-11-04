package net.cpollet.gallery.domain.picture.exceptions;

import net.cpollet.gallery.domain.picture.values.PictureId;

@Deprecated
public class MainImageMissingException extends RuntimeException {
    public MainImageMissingException(PictureId pictureId) {
        super(String.format("Main image is missing for picture %s", pictureId));
    }
}
