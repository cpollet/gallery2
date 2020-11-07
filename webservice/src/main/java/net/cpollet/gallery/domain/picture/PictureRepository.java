package net.cpollet.gallery.domain.picture;

import net.cpollet.gallery.domain.picture.entities.Picture;
import net.cpollet.gallery.domain.picture.values.PictureId;

import java.util.List;
import java.util.Optional;

public interface PictureRepository {
    Picture save(Picture picture);

    Optional<Picture> fetch(PictureId pictureId);

    List<Picture> all();
}
