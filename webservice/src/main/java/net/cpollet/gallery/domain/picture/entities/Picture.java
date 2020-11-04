package net.cpollet.gallery.domain.picture.entities;

import io.vavr.control.Either;
import lombok.EqualsAndHashCode;
import lombok.Value;
import net.cpollet.gallery.domain.common.values.Description;
import net.cpollet.gallery.domain.picture.PhysicalImage;
import net.cpollet.gallery.domain.picture.PhysicalImageFactory;
import net.cpollet.gallery.domain.picture.errors.DomainError;
import net.cpollet.gallery.domain.picture.errors.UnexpectedError;
import net.cpollet.gallery.domain.picture.exceptions.MainImageMissingException;
import net.cpollet.gallery.domain.picture.values.Dimension;
import net.cpollet.gallery.domain.picture.values.Name;
import net.cpollet.gallery.domain.picture.values.PictureId;
import net.cpollet.gallery.domain.picture.values.Role;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Value
@EqualsAndHashCode(of = "pictureId")
public class Picture {
    PictureId pictureId;
    Name name;
    Description description;
    LocalDateTime date;
    List<Image> images;

    public Picture(PictureId pictureId, Name name, Description description, LocalDateTime date, Collection<Image> images) {
        this.pictureId = pictureId;
        this.name = name;
        this.description = description;
        this.date = date;
        this.images = List.copyOf(images);
    }

    public Picture(Name name, Description description, LocalDateTime date, Collection<Image> images) {
        this(null, name, description, date, images);
    }

    public Optional<PictureId> getPictureId() {
        return Optional.ofNullable(pictureId);
    }

    public Picture updateDescription(Description newDescription) {
        return new Picture(
                pictureId, name, newDescription, date, images
        );
    }

    public Optional<Picture> generateThumbnail(Dimension dimension, PhysicalImageFactory physicalImageFactory) {
        return physicalImageFactory.create(getMain())
                .flatMap(i -> i.resize(dimension)
                        .map((Function<PhysicalImage, Either<DomainError, PhysicalImage>>) Either::right)
                        .orElseGet(() -> Either.left(new UnexpectedError()))
                )
                .map(i -> new Image(Role.THUMBNAIL, i.getBytes(), i.getFormat(), i.getDimension()))
                .map(this::addImage)
                .fold(// TODO we may want to bubble the domain error...
                        l -> Optional.empty(),
                        Optional::of
                );
    }

    public Picture addImage(Image image) {
        ArrayList<Image> newImages = new ArrayList<>(images);
        newImages.add(image);
        return new Picture(pictureId, name, description, date, newImages);
    }

    public Image getMain() {
        return images.stream()
                .filter(i -> i.getRole().equals(Role.MAIN))
                .findFirst()
                .orElseThrow(() -> new MainImageMissingException(pictureId)); // TODO get rid of this exception
    }

    public Optional<Image> getThumbnail() {
        return images.stream()
                .filter(i -> i.getRole().equals(Role.THUMBNAIL))
                .findFirst();
    }
}
