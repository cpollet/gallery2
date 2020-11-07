package net.cpollet.gallery.infrastructure.web.rest.data;

import lombok.Getter;
import lombok.Setter;
import net.cpollet.gallery.domain.picture.entities.Image;
import net.cpollet.gallery.domain.picture.entities.Picture;
import net.cpollet.gallery.domain.picture.values.ImageId;
import net.cpollet.gallery.domain.picture.values.PictureId;
import net.cpollet.gallery.infrastructure.web.rest.PictureController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

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
        RestPicture restPicture = new RestPicture(
                picture.getPictureId().map(PictureId::getId).orElseThrow(IllegalStateException::new),
                picture.getName().getName(),
                picture.getDescription().getDescription(),
                picture.getDescription().tags().stream()
                        .map(t -> new Tag(t.getStart(), t.getEnd()))
                        .collect(Collectors.toSet())
        );
        restPicture.add(
                WebMvcLinkBuilder.linkTo(PictureController.class).slash(restPicture.getId()).withSelfRel(),
                linkTo(restPicture, picture.getMainImage(), "main-bytes")
        );
        picture.getThumbnail().ifPresent(thumbnailImage ->
                restPicture.add(linkTo(restPicture, thumbnailImage, "thumbnail-bytes"))
        );
        return restPicture;
    }

    private static Link linkTo(RestPicture picture, Image image, String rel) {
        return WebMvcLinkBuilder.linkTo(PictureController.class)
                .slash(picture.getId())
                .slash("images")
                .slash(image.getImageId().map(ImageId::getId).orElseThrow(IllegalStateException::new))
                .withRel(rel);
    }
}
