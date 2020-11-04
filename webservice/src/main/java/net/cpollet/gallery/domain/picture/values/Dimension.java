package net.cpollet.gallery.domain.picture.values;

import lombok.Value;

@Value
public class Dimension {
    int height;
    int width;

    public Dimension(int width, int height) {
        // TODO get rid of exceptions...
        if (width <= 0) {
            throw new IllegalArgumentException("width must be positive. got " + width);
        }
        if (height <= 0) {
            throw new IllegalArgumentException("height must be positive. got " + height);
        }
        this.width = width;
        this.height = height;
    }

    public float getRatio() {
        return (float) width / height;
    }
}
