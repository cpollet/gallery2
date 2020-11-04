package net.cpollet.gallery.infrastructure.web.rest.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tag {
    private final int start;
    private final int end;

    public Tag(int start, int end) {
        this.start = start;
        this.end = end;
    }
}
