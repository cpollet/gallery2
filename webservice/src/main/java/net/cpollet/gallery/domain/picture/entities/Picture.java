package net.cpollet.gallery.domain.picture.entities;

import io.vavr.Tuple;
import lombok.EqualsAndHashCode;
import lombok.Value;
import net.cpollet.gallery.domain.common.values.Description;
import net.cpollet.gallery.domain.picture.PhysicalImageFactory;
import net.cpollet.gallery.domain.picture.exceptions.DomainException;
import net.cpollet.gallery.domain.picture.values.Dimension;
import net.cpollet.gallery.domain.picture.values.Name;
import net.cpollet.gallery.domain.picture.values.PictureId;
import net.cpollet.gallery.domain.picture.values.Role;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Value
@EqualsAndHashCode(of = "pictureId")
public class Picture {
    PictureId pictureId;
    Name name;
    Description description;
    LocalDateTime date;
    List<Image> images;

    public Picture(PictureId pictureId, Name name, Description description, LocalDateTime date, Collection<Image> images) {
        if (images.stream().filter(i -> i.getRole().equals(Role.MAIN)).count() != 1) {
            throw new DomainException(String.format(
                    "images must contain exactly 1 MAIN image in pictureId [%s]", pictureId
            ));
        }
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

    public Picture generateThumbnail(Dimension dimension, PhysicalImageFactory physicalImageFactory) {
        return Tuple.of(physicalImageFactory.create(getMainImage()).getOrElseThrow(DomainException::new))
                .map(i -> i.resize(dimension))
                .map(i -> new Image(Role.THUMBNAIL, i.getBytes(), i.getFormat(), i.getDimension()))
                .map(this::addImage)
                ._1();
    }

    public Picture addImage(Image image) {
        ArrayList<Image> newImages = new ArrayList<>(images);
        newImages.add(image);
        return new Picture(pictureId, name, description, date, newImages);
    }

    public Image getMainImage() {
        return images.stream()
                .filter(i -> i.getRole().equals(Role.MAIN))
                .findFirst()
                .orElseThrow();
    }

    public List<Image> getThumbnails() {
        return images.stream()
                .filter(i -> i.getRole().equals(Role.THUMBNAIL))
                .collect(Collectors.toList());
    }
}
