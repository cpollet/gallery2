package net.cpollet.gallery.infrastructure.web.rest.data;

import lombok.Getter;
import lombok.Setter;
import net.cpollet.gallery.domain.picture.entities.Picture;
import net.cpollet.gallery.domain.picture.values.PictureId;
import org.springframework.hateoas.RepresentationModel;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class RestPicture extends RepresentationModel<RestPicture> {
    private final long id;
    private final String name;
    private final String description;
    private final Set<Tag> tags;

    public RestPicture(long id, String name, String description, Set<Tag> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tags = tags;
    }

    public static RestPicture from(Picture picture) {
        return new RestPicture(
                picture.getPictureId().map(PictureId::getId).orElseThrow(IllegalStateException::new),
                picture.getName().getName(),
                picture.getDescription().getDescription(),
                picture.getDescription().tags().stream()
                        .map(t -> new Tag(t.getStart(), t.getEnd()))
                        .collect(Collectors.toSet())
        );
    }
}
