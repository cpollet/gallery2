package net.cpollet.gallery.infrastructure.exceptions;

public class InfrastructureException extends RuntimeException {
    public InfrastructureException(Throwable t) {
        super(t);
    }
}
