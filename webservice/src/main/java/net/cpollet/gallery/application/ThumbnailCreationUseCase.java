package net.cpollet.gallery.application;

import lombok.extern.slf4j.Slf4j;
import net.cpollet.gallery.domain.picture.PhysicalImageFactory;
import net.cpollet.gallery.domain.picture.PictureRepository;
import net.cpollet.gallery.domain.picture.entities.Picture;
import net.cpollet.gallery.domain.picture.values.Dimension;
import net.cpollet.gallery.domain.picture.values.PictureId;

import java.util.Optional;

@Slf4j
public class ThumbnailCreationUseCase {
    private final PictureRepository pictureRepository;
    private final PhysicalImageFactory physicalImageFactory;

    public ThumbnailCreationUseCase(PictureRepository pictureRepository, PhysicalImageFactory physicalImageFactory) {
        this.pictureRepository = pictureRepository;
        this.physicalImageFactory = physicalImageFactory;
    }

    public Optional<Picture> createThumbnail(PictureId pictureId, Dimension dimension) {
        log.info("Generating thumbnail for {}; dimension is {}", pictureId, dimension);
        return pictureRepository
                .fetch(pictureId)
                .map(p -> p.generateThumbnail(dimension, physicalImageFactory))
                .map(pictureRepository::save);
    }
}
