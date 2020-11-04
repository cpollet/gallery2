package net.cpollet.gallery.infrastructure.database.entities;

import lombok.NonNull;
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
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class DBImageTest {
    @Test
    void from() {
        long imageId = 1L;
        byte[] data = new byte[]{'A'};
        int width = 1;
        int height  = 2;
        long pictureId = 2L;

        DBImage image =DBImage.from(new Image(
                new ImageId(imageId),
                Role.MAIN,
                new Bytes(data),
                Format.JPEG,
                new Dimension(width, height)
        ), pictureId);

        Assertions.assertThat(image.getId()).isEqualTo(imageId);
        Assertions.assertThat(image.getPictureId()).isEqualTo(pictureId);
        Assertions.assertThat(image.getRole()).isEqualTo(Role.MAIN.name());
        Assertions.assertThat(image.getData()).isEqualTo(data);
        Assertions.assertThat(image.getFormat()).isEqualTo(Format.JPEG.name());
        Assertions.assertThat(image.getWidth()).isEqualTo(width);
        Assertions.assertThat(image.getHeight()).isEqualTo(height);
    }

    @Test
    void toDomain() {
        long imageId = 1L;
        long pictureId = 2L;
        String role = Role.MAIN.name();
        byte[] data = new byte[]{'A'};
        String format = Format.JPEG.name();
        int width = 1;
        int height  = 2;

        Image image = new DBImage(imageId, pictureId, role, data, format, width, height).toDomain();

        Assertions.assertThat(image.getImageId()).get().isEqualTo(new ImageId(imageId));
        Assertions.assertThat(image.getRole()).isEqualTo(Role.MAIN);
        Assertions.assertThat(image.getData()).isEqualTo(new Bytes(data));
        Assertions.assertThat(image.getFormat()).isEqualTo(Format.JPEG);
        Assertions.assertThat(image.getDimension()).isEqualTo(new Dimension(width, height));
    }
}