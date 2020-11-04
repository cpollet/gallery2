package net.cpollet.gallery.infrastructure.database;

import net.cpollet.gallery.domain.common.values.Description;
import net.cpollet.gallery.domain.picture.entities.Image;
import net.cpollet.gallery.domain.picture.entities.Picture;
import net.cpollet.gallery.domain.picture.values.Bytes;
import net.cpollet.gallery.domain.picture.values.Dimension;
import net.cpollet.gallery.domain.picture.values.Format;
import net.cpollet.gallery.domain.picture.values.ImageId;
import net.cpollet.gallery.domain.picture.values.Name;
import net.cpollet.gallery.domain.picture.values.PictureId;
import net.cpollet.gallery.domain.picture.values.Role;
import net.cpollet.gallery.infrastructure.database.entities.DBImage;
import net.cpollet.gallery.infrastructure.database.entities.DBPicture;
import net.cpollet.gallery.infrastructure.database.repositories.SpringDataJdbcImageRepository;
import net.cpollet.gallery.infrastructure.database.repositories.SpringDataJdbcPictureRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.function.LongSupplier;

@ExtendWith(MockitoExtension.class)
class DatabasePictureRepositoryTest {
    private DatabasePictureRepository repository;

    private LongSupplier idGenerator;

    @Mock
    private SpringDataJdbcPictureRepository pictureRepository;

    @Mock
    private SpringDataJdbcImageRepository imageRepository;

    @BeforeEach
    void setup() {
        repository = new DatabasePictureRepository(pictureRepository, imageRepository);

        idGenerator = new LongSupplier() {
            long current = 0;

            @Override
            public long getAsLong() {
                return current++;
            }
        };
    }

    @Test
    void save() {
        Mockito.when(pictureRepository.save(ArgumentMatchers.any(DBPicture.class)))
                .then((Answer<DBPicture>) invocation -> {
                    DBPicture picture = invocation.getArgument(0);
                    picture.setId(idGenerator.getAsLong());
                    return picture;
                });

        Mockito.when(imageRepository.save(ArgumentMatchers.any(DBImage.class)))
                .then((Answer<DBImage>) invocation -> {
                    DBImage image = invocation.getArgument(0);
                    image.setId(idGenerator.getAsLong());
                    return image;
                });

        Image image = new Image(Role.MAIN, new Bytes(new byte[]{}), Format.JPEG, new Dimension(1, 2));
        Picture picture = new Picture(
                new Name("name"),
                new Description("description"),
                LocalDateTime.now(),
                Collections.singletonList(image)
        );

        Optional<Picture> savedPicture = repository.save(picture);

        Assertions.assertThat(savedPicture)
                .get()
                .isNotEqualTo(picture)
                .isNotSameAs(picture)
                .extracting(Picture::getPictureId, InstanceOfAssertFactories.optional(PictureId.class))
                .get()
                .isEqualTo(new PictureId(0L));

        Assertions.assertThat(savedPicture)
                .get()
                .extracting(Picture::getImages, InstanceOfAssertFactories.list(Image.class))
                .hasSize(1)
                .element(0)
                .extracting(Image::getImageId, InstanceOfAssertFactories.optional(ImageId.class))
                .get()
                .isEqualTo(new ImageId(1L));
    }

    @Test
    void fetch() {
        long pictureId = 1L;
        long imageId = 2L;

        Mockito.when(pictureRepository.findById(pictureId))
                .thenReturn(Optional.of(
                        new DBPicture(pictureId, "name", "description", LocalDateTime.now())
                ));
        Mockito.when(imageRepository.findByPictureId(pictureId))
                .thenReturn(Collections.singleton(
                        new DBImage(imageId, pictureId, "MAIN", new byte[]{}, "JPEG", 1, 2)
                ));

        Optional<Picture> picture = repository.fetch(new PictureId(pictureId));

        Assertions.assertThat(picture)
                .get()
                .extracting(Picture::getPictureId, InstanceOfAssertFactories.optional(PictureId.class))
                .get()
                .isEqualTo(new PictureId(pictureId));

        Assertions.assertThat(picture)
                .get()
                .extracting(Picture::getImages, InstanceOfAssertFactories.list(Image.class))
                .hasSize(1)
                .element(0)
                .extracting(Image::getImageId, InstanceOfAssertFactories.optional(ImageId.class))
                .get()
                .isEqualTo(new ImageId(imageId));
    }
}