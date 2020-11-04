package net.cpollet.gallery.infrastructure.spring;

import net.cpollet.gallery.domain.picture.PictureRepository;
import net.cpollet.gallery.infrastructure.database.DatabasePictureRepository;
import net.cpollet.gallery.infrastructure.database.repositories.SpringDataJdbcImageRepository;
import net.cpollet.gallery.infrastructure.database.repositories.SpringDataJdbcPictureRepository;
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
}
