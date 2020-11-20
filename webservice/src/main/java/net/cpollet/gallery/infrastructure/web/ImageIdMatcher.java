package net.cpollet.gallery.infrastructure.web;

import net.cpollet.gallery.domain.picture.entities.Image;
import net.cpollet.gallery.domain.picture.values.ImageId;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public final class ImageIdMatcher implements Predicate< Image> {
    private static final BiPredicate<String, Image> ROLE_MATCHER = (String s, Image i) ->
            i.getRole().name().toLowerCase().equals(s);
    private static final BiPredicate<String, Image> IMAGEID_MATCHER = (String s, Image i) ->
            i.getImageId()
                    .map(imageId -> isEquals(s, imageId))
                    .orElse(false);
    private final String imageId;

    public ImageIdMatcher(String imageId) {
        this.imageId = imageId;
    }

    private static boolean isEquals(String string, ImageId imageId) {
        try {
            return imageId.equals(new ImageId(Long.parseLong(string)));
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public boolean test(Image image) {
        if (imageId.matches("[0-9]+")) {
            return IMAGEID_MATCHER.test(imageId, image);
        }
        return ROLE_MATCHER.test(imageId, image);
    }
}
