package net.cpollet.gallery.infrastructure.database;

import lombok.extern.slf4j.Slf4j;
import net.cpollet.gallery.domain.picture.PictureRepository;
import net.cpollet.gallery.domain.picture.entities.Picture;
import net.cpollet.gallery.domain.picture.values.PictureId;
import net.cpollet.gallery.infrastructure.database.entities.DBImage;
import net.cpollet.gallery.infrastructure.database.entities.DBPicture;
import net.cpollet.gallery.infrastructure.database.repositories.SpringDataJdbcImageRepository;
import net.cpollet.gallery.infrastructure.database.repositories.SpringDataJdbcPictureRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class DatabasePictureRepository implements PictureRepository {
    private final SpringDataJdbcPictureRepository pictureRepository;
    private final SpringDataJdbcImageRepository imageRepository;

    public DatabasePictureRepository(
            SpringDataJdbcPictureRepository pictureRepository,
            SpringDataJdbcImageRepository imageRepository
    ) {
        this.pictureRepository = pictureRepository;
        this.imageRepository = imageRepository;
    }

    @Override
    public Optional<Picture> save(Picture picture) {
        try {
            DBPicture dbPicture = pictureRepository.save(DBPicture.from(picture));

            List<DBImage> images = picture.getImages().stream()
                    .map(image -> imageRepository.save(DBImage.from(image, dbPicture.getId())))
                    .collect(Collectors.toList());

            return Optional.of(dbPicture.toDomain(images));
        }
        catch (Throwable t) {
            log.error("Unable to save picture {}", picture.getPictureId(), t);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Picture> fetch(PictureId pictureId) {
        try {
            return pictureRepository
                    .findById(pictureId.getId())
                    .map(picture -> picture.toDomain(imageRepository.findByPictureId(picture.getId())));
        }
        catch (Throwable t) {
            log.error("Unable to fetch picture {}", pictureId, t);
            return Optional.empty();
        }
    }
}
