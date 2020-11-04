package net.cpollet.gallery.infrastructure.database.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.cpollet.gallery.domain.picture.entities.Image;
import net.cpollet.gallery.domain.picture.values.Bytes;
import net.cpollet.gallery.domain.picture.values.Dimension;
import net.cpollet.gallery.domain.picture.values.Format;
import net.cpollet.gallery.domain.picture.values.ImageId;
import net.cpollet.gallery.domain.picture.values.Role;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@Table("images")
public class DBImage {
    @Id
    private Long id;
    private long pictureId;
    private String role;
    private byte[] data;
    private String format;
    private int width;
    private int height;

    public static DBImage from(Image image, long pictureId) {
        return new DBImage(
                image.getImageId().map(ImageId::getId).orElse(null),
                pictureId,
                image.getRole().name(),
                image.getData().getBytes(),
                image.getFormat().name(),
                image.getDimension().getWidth(),
                image.getDimension().getHeight()
        );
    }

    public Image toDomain() {
        return new Image(
                new ImageId(id),
                Role.valueOf(role),
                new Bytes(data),
                Format.valueOf(format),
                new Dimension(width, height)
        );
    }
}
