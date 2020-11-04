package net.cpollet.gallery.infrastructure.web.rest;

import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import net.cpollet.gallery.domain.common.values.Description;
import net.cpollet.gallery.domain.picture.PictureRepository;
import net.cpollet.gallery.domain.picture.entities.Image;
import net.cpollet.gallery.domain.picture.entities.Picture;
import net.cpollet.gallery.domain.picture.values.Bytes;
import net.cpollet.gallery.domain.picture.values.ImageId;
import net.cpollet.gallery.domain.picture.values.Name;
import net.cpollet.gallery.domain.picture.values.PictureId;
import net.cpollet.gallery.domain.picture.values.Role;
import net.cpollet.gallery.infrastructure.PhysicalImageFactory;
import net.cpollet.gallery.infrastructure.web.rest.data.RestPicture;
import net.cpollet.gallery.infrastructure.web.rest.data.Step;
import net.cpollet.gallery.infrastructure.web.rest.requests.CreatePictureRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@RestController
@RequestMapping(value = "/pictures")
@Slf4j
public class PictureController {
    private final PictureRepository pictureRepository;
    private final RestPictureRepository restPictureRepository;
    private final PhysicalImageFactory physicalImageFactory;

    @Autowired
    public PictureController(
            PictureRepository pictureRepository,
            RestPictureRepository restPictureRepository,
            PhysicalImageFactory physicalImageFactory
    ) {
        this.pictureRepository = pictureRepository;
        this.restPictureRepository = restPictureRepository;
        this.physicalImageFactory = physicalImageFactory;
    }

    @PostMapping("/")
    public ResponseEntity<Step> createPicture(@RequestBody CreatePictureRequest createPictureRequest) {
        UUID uuid = restPictureRepository.save(new Picture(
                new Name(createPictureRequest.getName()),
                new Description(createPictureRequest.getDescription()),
                LocalDateTime.now(),
                Collections.emptyList()
        ));

        return ResponseEntity.ok().body(new Step(uuid)
                .add(WebMvcLinkBuilder.linkTo(PictureController.class)
                        .slash("pending")
                        .slash(uuid)
                        .withRel("upload-fist-image"))
        );
    }

    @PutMapping("/pending/{uuid}")
    public ResponseEntity<RestPicture> uploadMainImage(@PathVariable UUID uuid, @RequestBody byte[] bytes) {
        return restPictureRepository.fetch(uuid)
                .map((Function<Picture, Either<ResponseEntity<RestPicture>, Picture>>) Either::right)
                .orElseGet(() -> Either.left(ResponseEntity.notFound().build()))
                .flatMap(picture -> physicalImageFactory.create(new Bytes(bytes))
                        .map(physicalImage -> picture.addImage(new Image(
                                Role.MAIN,
                                physicalImage.getBytes(),
                                physicalImage.getFormat(),
                                physicalImage.getDimension()
                        )))
                        .mapLeft(e -> ResponseEntity.badRequest().build())
                )
                .flatMap(picture -> pictureRepository.save(picture)
                        .map(this::toPictureResponse)
                        .map((Function<RestPicture, Either<ResponseEntity<RestPicture>, RestPicture>>) Either::right)
                        .orElseGet(() -> Either.left(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()))
                )
                .fold(
                        l -> l,
                        ResponseEntity::ok
                );
    }

    private RestPicture toPictureResponse(Picture picture) {
        RestPicture restPicture = RestPicture.from(picture);
        restPicture.add(
                WebMvcLinkBuilder.linkTo(PictureController.class).slash(restPicture.getId()).withSelfRel(),
                linkTo(restPicture, picture.getMain(), "main-data")
        );
        picture.getThumbnail().ifPresent(t ->
                restPicture.add(linkTo(restPicture, t, "thumbnail-data"))
        );

        return restPicture;
    }

    @GetMapping("/{pictureId}")
    public ResponseEntity<RestPicture> getPictureById(@PathVariable long pictureId) {
        PictureId _pictureId = new PictureId(pictureId);

        log.info("fetching picture {}", _pictureId);

        return pictureRepository.fetch(_pictureId)
                .map(this::toPictureResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private Link linkTo(RestPicture picture, Image image, String rel) {
        return WebMvcLinkBuilder.linkTo(PictureController.class)
                .slash(picture.getId())
                .slash("images")
                .slash(image.getImageId().map(ImageId::getId).orElseThrow(IllegalStateException::new))
                .withRel(rel);
    }

    @GetMapping("/{pictureId}/images/{imageId}")
    public ResponseEntity<Object> getImageByPictureIdAndById(
            @PathVariable long pictureId,
            @PathVariable long imageId,
            @RequestParam(name = "encoding", required = false, defaultValue = "raw") String encoding
    ) {
        PictureId _pictureId = new PictureId(pictureId);
        ImageId _imageId = new ImageId(imageId);
        Encoding _encoding = Encoding.from(encoding).orElse(Encoding.RAW);

        log.info("fetching image {}/{} - {}", _pictureId, _imageId, _encoding);

        Optional<Picture> picture = pictureRepository.fetch(new PictureId(pictureId));

        return picture
                .flatMap(p -> p.getImages().stream()
                        .filter(i -> i.getImageId().isPresent() && i.getImageId().get().equals(_imageId))
                        .findFirst()
                )
                .map(i -> ResponseEntity.ok()
                        .contentType(_encoding.contentType(i.getFormat()))
                        .body(_encoding.encode(i.getData()))
                )
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
