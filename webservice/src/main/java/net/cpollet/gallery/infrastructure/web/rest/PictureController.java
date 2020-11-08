package net.cpollet.gallery.infrastructure.web.rest;

import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import net.cpollet.gallery.application.PictureCreationUseCase;
import net.cpollet.gallery.domain.common.values.Description;
import net.cpollet.gallery.domain.picture.PictureRepository;
import net.cpollet.gallery.domain.picture.values.Bytes;
import net.cpollet.gallery.domain.picture.values.Name;
import net.cpollet.gallery.domain.picture.values.PictureId;
import net.cpollet.gallery.infrastructure.io.FileDownloader;
import net.cpollet.gallery.infrastructure.web.rest.data.RestPicture;
import net.cpollet.gallery.infrastructure.web.rest.data.Step;
import net.cpollet.gallery.infrastructure.web.rest.requests.CreatePictureRequest;
import net.cpollet.gallery.infrastructure.web.rest.requests.CreatePictureResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/pictures")
@CrossOrigin(origins = "*")
@Slf4j
public class PictureController {
    private final PictureRepository pictureRepository;
    private final RestPictureRepository restPictureRepository;
    private final PictureCreationUseCase pictureCreationUseCase;
    private final FileDownloader fileDownloader;

    @Autowired
    public PictureController(
            PictureRepository pictureRepository,
            RestPictureRepository restPictureRepository,
            PictureCreationUseCase pictureCreationUseCase,
            FileDownloader fileDownloader) {
        this.pictureRepository = pictureRepository;
        this.restPictureRepository = restPictureRepository;
        this.pictureCreationUseCase = pictureCreationUseCase;
        this.fileDownloader = fileDownloader;
    }

    @GetMapping
    public ResponseEntity<List<RestPicture>> getAllPictures() {
        log.info("Retrieving all pictures");
        return ResponseEntity.ok(
                pictureRepository.all().stream()
                        .map(RestPicture::from)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping
    public ResponseEntity<CreatePictureResponse> createPicture(@RequestBody CreatePictureRequest createPictureRequest) {
        if (createPictureRequest.getUrl() != null) {
            byte[] bytes = fileDownloader.getBytesFromUrl(createPictureRequest.getUrl());

            return pictureCreationUseCase.createPicture(
                    new Name(createPictureRequest.getName()),
                    new Description(createPictureRequest.getDescription()),
                    new Bytes(bytes)
            )
                    .mapLeft(e -> ResponseEntity.badRequest().<CreatePictureResponse>build())
                    .map(RestPicture::from)
                    .map(CreatePictureResponse::new)
                    .map(ResponseEntity::ok)
                    .fold(l -> l, r -> r);
        }

        UUID uuid = restPictureRepository.push(createPictureRequest);

        return ResponseEntity.ok(new CreatePictureResponse(new Step(uuid)
                .add(WebMvcLinkBuilder
                        .linkTo(methodOn(PictureController.class).createPicture(null))
                        .withSelfRel())
                .add(WebMvcLinkBuilder
                        .linkTo(methodOn(PictureController.class).uploadMainImage(uuid, null))
                        .withRel("main-image"))
        ));
    }

    @DeleteMapping("/pending/{uuid}")
    public ResponseEntity<Void> deletePendingPicture(@PathVariable UUID uuid) {
        restPictureRepository.pull(uuid);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/pending/{uuid}")
    public ResponseEntity<RestPicture> uploadMainImage(@PathVariable UUID uuid, @RequestBody byte[] bytes) {
        return restPictureRepository.pull(uuid)
                .map(newPictureInfo -> pictureCreationUseCase.createPicture(
                        new Name(newPictureInfo.getName()),
                        new Description(newPictureInfo.getDescription()),
                        new Bytes(bytes)
                ))
                .map(e -> e.mapLeft(domainError -> ResponseEntity.badRequest().<RestPicture>build()))
                .orElseGet(() -> Either.left(ResponseEntity.notFound().build()))
                .map(RestPicture::from)
                .map(ResponseEntity::ok)
                .fold(l -> l, r -> r);
    }

    @GetMapping("/{pictureId}")
    public ResponseEntity<RestPicture> getPictureById(@PathVariable long pictureId) {
        PictureId _pictureId = new PictureId(pictureId);

        log.info("fetching picture {}", _pictureId);

        return pictureRepository.fetch(_pictureId)
                .map(RestPicture::from)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{pictureId}/images/{imageId}")
    public ResponseEntity<Object> getImageByPictureIdAndById(
            @PathVariable long pictureId,
            @PathVariable String imageId,
            @RequestParam(name = "encoding", required = false, defaultValue = "raw") String encoding
    ) {
        Encoding _encoding = Encoding.from(encoding).orElse(Encoding.RAW);

        log.info("fetching image {}/{} - {}", pictureId, imageId, _encoding);

        ImageIdMatcher imageIdMatcher = new ImageIdMatcher(imageId);

        return pictureRepository.fetch(new PictureId(pictureId))
                .flatMap(p -> p.getImages().stream()
                        .filter(imageIdMatcher)
                        .findFirst()
                )
                .map(i -> ResponseEntity.ok()
                        .contentType(_encoding.contentType(i.getFormat()))
                        .body(_encoding.encode(i.getData()))
                )
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}

