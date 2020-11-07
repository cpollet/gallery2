package net.cpollet.gallery.domain.picture.exceptions;

import net.cpollet.gallery.domain.picture.errors.DomainError;

import java.util.Optional;

public class DomainException extends RuntimeException {
    private final DomainError domainError;

    public DomainException(String message, DomainError domainError) {
        super(message);
        this.domainError = domainError;
    }

    public DomainException(DomainError domainError) {
        this(domainError.toString(), domainError);
    }

    public DomainException(String message) {
        this(message, null);
    }

    public Optional<DomainError> getDomainError() {
        return Optional.ofNullable(domainError);
    }
}
