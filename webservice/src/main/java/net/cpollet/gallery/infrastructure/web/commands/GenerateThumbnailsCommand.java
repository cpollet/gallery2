package net.cpollet.gallery.infrastructure.web.commands;

import lombok.Getter;
import net.cpollet.gallery.application.ThumbnailCreationUseCase;
import net.cpollet.gallery.domain.picture.values.Dimension;
import net.cpollet.gallery.domain.picture.values.PictureId;
import net.cpollet.gallery.infrastructure.web.rest.data.RestPicture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(value = "/commands/generateThumbnail")
@CrossOrigin(origins = "*")
public class GenerateThumbnailsCommand {
    private final ThumbnailCreationUseCase thumbnailCreationUseCase;

    @Autowired
    public GenerateThumbnailsCommand(ThumbnailCreationUseCase thumbnailCreationUseCase) {
        this.thumbnailCreationUseCase = thumbnailCreationUseCase;
    }

    @PutMapping("{uuid}")
    public ResponseEntity<GenerateThumbnailResponse> generateThumbnail(
            @PathVariable UUID uuid,
            @RequestBody GenerateThumbnailRequest request
    ) {
        return ResponseEntity.ok(
                thumbnailCreationUseCase
                        .createThumbnail(
                                new PictureId(request.pictureId),
                                new Dimension(request.width, request.height)
                        )
                        .map(RestPicture::from)
                        .map(GenerateThumbnailResponse::success)
                        .orElseGet(GenerateThumbnailResponse::rejected)
        );
    }

    static class GenerateThumbnailRequest {
        public long pictureId;
        public int width;
        public int height;
    }

    @Getter
    static class GenerateThumbnailResponse {
        private final String status;
        private final RestPicture picture;

        public GenerateThumbnailResponse(RestPicture picture, String status) {
            this.picture = picture;
            this.status = status;
        }

        public GenerateThumbnailResponse(String status) {
            this(null, status);
        }

        public static GenerateThumbnailResponse success(RestPicture picture) {
            return new GenerateThumbnailResponse(picture, "SUCCESS");
        }

        public static GenerateThumbnailResponse rejected() {
            return new GenerateThumbnailResponse("REJECTED");
        }
    }
}
