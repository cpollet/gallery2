package net.cpollet.gallery.infrastructure.web.rest.data;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

@Getter
@Setter
public class Step extends RepresentationModel<Step>{
    private final UUID uuid;

    public Step(UUID uuid) {
        this.uuid = uuid;
    }
}
