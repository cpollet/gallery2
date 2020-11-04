package net.cpollet.gallery.domain.common.values;

import lombok.NonNull;
import lombok.Value;

@Value
public class Tag {
    @NonNull String tag;
    int start;
    int end;
}
