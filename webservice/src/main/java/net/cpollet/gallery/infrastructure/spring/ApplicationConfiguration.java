package net.cpollet.gallery.infrastructure.spring;

import net.cpollet.gallery.application.PictureCreationUseCase;
import net.cpollet.gallery.application.ThumbnailCreationUseCase;
import net.cpollet.gallery.domain.picture.PictureRepository;
import net.cpollet.gallery.infrastructure.AWTPhysicalImageFactory;
import net.cpollet.gallery.infrastructure.PhysicalImageFactory;
import net.cpollet.gallery.infrastructure.database.DatabasePictureRepository;
import net.cpollet.gallery.infrastructure.database.repositories.SpringDataJdbcImageRepository;
import net.cpollet.gallery.infrastructure.database.repositories.SpringDataJdbcPictureRepository;
import net.cpollet.gallery.infrastructure.io.FileDownloader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {
    @Bean
    PictureRepository pictureRepository(
            SpringDataJdbcPictureRepository springDataJdbcPictureRepository,
            SpringDataJdbcImageRepository springDataJdbcImageRepository
    ) {
        return new DatabasePictureRepository(springDataJdbcPictureRepository, springDataJdbcImageRepository);
    }

    @Bean
    PhysicalImageFactory physicalImageFactory() {
        return new AWTPhysicalImageFactory();
    }

    @Bean
    FileDownloader fileDownloader() {
        return new FileDownloader(5000, 5000);
    }

    @Bean
    PictureCreationUseCase pictureCreationUseCase(
            PhysicalImageFactory physicalImageFactory,
            PictureRepository pictureRepository
    ) {
        return new PictureCreationUseCase(physicalImageFactory, pictureRepository);
    }

    @Bean
    ThumbnailCreationUseCase thumbnailCreationUseCase(
            PictureRepository pictureRepository,
            PhysicalImageFactory physicalImageFactory
    ) {
        return new ThumbnailCreationUseCase(pictureRepository, physicalImageFactory);
    }
}
