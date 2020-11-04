package net.cpollet.gallery.infrastructure.database.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.cpollet.gallery.domain.common.values.Description;
import net.cpollet.gallery.domain.picture.entities.Picture;
import net.cpollet.gallery.domain.picture.values.Name;
import net.cpollet.gallery.domain.picture.values.PictureId;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Table("pictures")
public class DBPicture {
    @Id
    private Long id;
    private String name;
    private String description;
    private LocalDateTime date;

    public static DBPicture from(Picture picture) {
        return new DBPicture(
                picture.getPictureId().map(PictureId::getId).orElse(null),
                picture.getName().getName(),
                picture.getDescription().getDescription(),
                picture.getDate()
        );
    }

    public Picture toDomain(Collection<DBImage> images) {
        return new Picture(
                new PictureId(id),
                new Name(name),
                new Description(description),
                LocalDateTime.from(date),
                images.stream().map(DBImage::toDomain).collect(Collectors.toList())
        );
    }
}
