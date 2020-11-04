package net.cpollet.gallery.domain.picture.values;

import lombok.NonNull;
import lombok.Value;

@Value
public class Name {
   @NonNull String name;
}
