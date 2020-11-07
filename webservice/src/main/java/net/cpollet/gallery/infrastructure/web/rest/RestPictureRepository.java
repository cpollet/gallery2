package net.cpollet.gallery.infrastructure.web.rest;

import net.cpollet.gallery.infrastructure.web.rest.requests.CreatePictureRequest;

import java.util.Optional;
import java.util.UUID;

public interface RestPictureRepository {
    UUID save(CreatePictureRequest picture);

    Optional<CreatePictureRequest> fetch(UUID uuid);
}
