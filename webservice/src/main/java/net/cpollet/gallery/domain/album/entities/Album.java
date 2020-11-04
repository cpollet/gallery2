package net.cpollet.gallery.domain.album.entities;

import lombok.EqualsAndHashCode;
import lombok.Value;
import net.cpollet.gallery.domain.album.values.AlbumId;
import net.cpollet.gallery.domain.common.values.Description;
import net.cpollet.gallery.domain.picture.values.PictureId;

import java.util.List;

@Value
@EqualsAndHashCode(of="albumId")
public class Album {
    AlbumId albumId;
    Description description;
    List<PictureId> pictures;
}
