package net.cpollet.gallery.application;

import net.cpollet.gallery.domain.common.values.Description;
import net.cpollet.gallery.domain.picture.PhysicalImage;
import net.cpollet.gallery.domain.picture.PhysicalImageFactory;
import net.cpollet.gallery.domain.picture.PictureRepository;
import net.cpollet.gallery.domain.picture.entities.Image;
import net.cpollet.gallery.domain.picture.entities.Picture;
import net.cpollet.gallery.domain.picture.values.Dimension;
import net.cpollet.gallery.domain.picture.values.Name;
import net.cpollet.gallery.domain.picture.values.Role;

import java.time.LocalDateTime;
import java.util.Collections;

public class ImportPicture {
    private final PictureRepository pictureRepository;
    private final PhysicalImageFactory physicalImageFactory;

    public ImportPicture(PictureRepository pictureRepository, PhysicalImageFactory physicalImageFactory) {
        this.pictureRepository = pictureRepository;
        this.physicalImageFactory = physicalImageFactory;
    }

    public Picture importPicture(PhysicalImage image, Name name, Description description, Dimension thumbnailDimension) {
        return pictureRepository.save(
                new Picture(
                        name,
                        description,
                        LocalDateTime.now(),
                        Collections.singletonList(
                                new Image(Role.MAIN, image.getBytes(), image.getFormat(), image.getDimension())
                        )
                ).generateThumbnail(thumbnailDimension, physicalImageFactory)
        );
    }
}
