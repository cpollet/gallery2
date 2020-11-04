package net.cpollet.gallery.infrastructure.database.entities;

import net.cpollet.gallery.domain.common.values.Description;
import net.cpollet.gallery.domain.picture.entities.Picture;
import net.cpollet.gallery.domain.picture.values.Name;
import net.cpollet.gallery.domain.picture.values.PictureId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;

class DBPictureTest {
    @Test
    void from() {
        long pictureId = 1L;
        String name = "name";
        String description = "desc";
        LocalDateTime date = LocalDateTime.now();

        DBPicture picture = DBPicture.from(new Picture(
                new PictureId(pictureId),
                new Name(name),
                new Description(description),
                date,
                Collections.emptyList()
        ));

        Assertions.assertThat(picture.getId()).isEqualTo(pictureId);
        Assertions.assertThat(picture.getName()).isEqualTo(name);
        Assertions.assertThat(picture.getDescription()).isEqualTo(description);
        Assertions.assertThat(picture.getDate()).isEqualTo(date);
    }

    @Test
    void toDomain() {
        long pictureId = 1L;
        String name = "name";
        String description = "desc";
        LocalDateTime date = LocalDateTime.now();

        Picture picture = new DBPicture(pictureId, name, description, date).toDomain(Collections.emptyList());

        Assertions.assertThat(picture.getPictureId()).get().isEqualTo(new PictureId(pictureId));
        Assertions.assertThat(picture.getName()).isEqualTo(new Name(name));
        Assertions.assertThat(picture.getDescription()).isEqualTo(new Description(description));
        Assertions.assertThat(picture.getDate()).isEqualTo(date);
    }
}