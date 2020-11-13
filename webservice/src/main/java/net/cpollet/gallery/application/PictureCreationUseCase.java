package net.cpollet.gallery.application;

import io.vavr.control.Either;
import net.cpollet.gallery.domain.common.values.Description;
import net.cpollet.gallery.domain.picture.PictureRepository;
import net.cpollet.gallery.domain.picture.entities.Image;
import net.cpollet.gallery.domain.picture.entities.Picture;
import net.cpollet.gallery.domain.picture.errors.DomainError;
import net.cpollet.gallery.domain.picture.values.Bytes;
import net.cpollet.gallery.domain.picture.values.Color;
import net.cpollet.gallery.domain.picture.values.Dimension;
import net.cpollet.gallery.domain.picture.values.Name;
import net.cpollet.gallery.domain.picture.values.Role;
import net.cpollet.gallery.infrastructure.PhysicalImageFactory;

import java.time.LocalDateTime;
import java.util.Collections;

public class PictureCreationUseCase {
    private final PhysicalImageFactory physicalImageFactory;
    private final PictureRepository pictureRepository;

    public PictureCreationUseCase(PhysicalImageFactory physicalImageFactory, PictureRepository pictureRepository) {
        this.physicalImageFactory = physicalImageFactory;
        this.pictureRepository = pictureRepository;
    }

    public Either<DomainError, Picture> createPicture(Name name, Description description, Bytes bytes) {
        return physicalImageFactory
                .create(bytes)
                .map(image -> new Picture(
                        name,
                        description,
                        LocalDateTime.now(),
                        Collections.singletonList(
                                new Image(
                                        Role.MAIN,
                                        image.getBytes(),
                                        image.getFormat(),
                                        image.getDimension(),
                                        Color.NONE
                                )
                        )
                ))
                .map(p -> p.generateThumbnail(new Dimension(450, 450), Color.WHITE, physicalImageFactory))
                .map(pictureRepository::save);
    }
}
