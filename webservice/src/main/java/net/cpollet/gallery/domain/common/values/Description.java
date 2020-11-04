package net.cpollet.gallery.domain.common.values;

import lombok.NonNull;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Value
public class Description {
    private static final Pattern TAG_PATTERN = Pattern.compile("#[_0-9a-zA-Z]+");

    @NonNull String description;

    public List<Tag> tags() {
        Matcher matcher = TAG_PATTERN.matcher(description);
        ArrayList<Tag> result = new ArrayList<>();

        while (matcher.find()) {
            result.add(new Tag(matcher.group().substring(1), matcher.start(), matcher.end()));
        }

        return result;
    }
}
