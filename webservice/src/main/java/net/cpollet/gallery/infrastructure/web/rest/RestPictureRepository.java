package net.cpollet.gallery.infrastructure.web.rest;

import net.cpollet.gallery.domain.picture.entities.Picture;

import java.util.Optional;
import java.util.UUID;

public interface RestPictureRepository {
    UUID save(Picture picture);

    Optional<Picture> fetch(UUID uuid);
}
