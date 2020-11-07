package net.cpollet.gallery.domain.picture.errors;

public class FormatNotSupported implements DomainError {
    private final String message;

    public FormatNotSupported(String formatName) {
        this.message = String.format("Format [%s] is not supported", formatName);
    }

    public FormatNotSupported() {
        this("unknown");
    }

    @Override
    public String message() {
        return message;
    }
}
