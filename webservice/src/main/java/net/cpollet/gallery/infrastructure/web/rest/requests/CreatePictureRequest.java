package net.cpollet.gallery.infrastructure.web.rest.requests;

import lombok.Getter;

@Getter
public class CreatePictureRequest {
    private String description;
    private String name;
    private String url;
}
