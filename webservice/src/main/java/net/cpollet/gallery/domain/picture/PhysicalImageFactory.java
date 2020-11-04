package net.cpollet.gallery.domain.picture;

import io.vavr.control.Either;
import net.cpollet.gallery.domain.picture.entities.Image;
import net.cpollet.gallery.domain.picture.errors.DomainError;

public interface PhysicalImageFactory {
    Either<DomainError, PhysicalImage> create(Image image);
}
