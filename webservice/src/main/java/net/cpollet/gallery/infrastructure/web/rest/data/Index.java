package net.cpollet.gallery.infrastructure.web.rest.data;

import net.cpollet.gallery.infrastructure.web.rest.PictureController;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

public class Index extends RepresentationModel<RestPicture> {
    public String index = "use the following links to discover the API";
    public String version = "1.0";

    public Index() {
        add(
                WebMvcLinkBuilder.linkTo(PictureController.class).withRel("pictures")
        );
    }
}
