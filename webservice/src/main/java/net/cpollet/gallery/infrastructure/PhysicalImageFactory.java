package net.cpollet.gallery.infrastructure;

import io.vavr.control.Either;
import net.cpollet.gallery.domain.picture.PhysicalImage;
import net.cpollet.gallery.domain.picture.errors.DomainError;
import net.cpollet.gallery.domain.picture.values.Bytes;

import java.util.Optional;

public interface PhysicalImageFactory extends net.cpollet.gallery.domain.picture.PhysicalImageFactory {
    Either<DomainError, PhysicalImage> create(Bytes bytes);
}
