package net.cpollet.gallery.infrastructure.web.rest.requests;

import lombok.Getter;
import net.cpollet.gallery.infrastructure.web.rest.data.RestPicture;
import net.cpollet.gallery.infrastructure.web.rest.data.Step;

@Getter
public class CreatePictureResponse {
    private final Step step;
    private final RestPicture picture;

    private CreatePictureResponse(Step step, RestPicture picture) {
        this.step = step;
        this.picture = picture;
    }

    public CreatePictureResponse(Step step) {
        this(step, null);
    }

    public CreatePictureResponse(RestPicture picture) {
        this(null, picture);
    }
}
