package net.cpollet.gallery.infrastructure.spring;


import net.cpollet.gallery.domain.picture.entities.Picture;
import net.cpollet.gallery.infrastructure.web.rest.PictureController;
import net.cpollet.gallery.infrastructure.web.rest.RestPictureRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Configuration
@EnableWebMvc
@ComponentScan(basePackageClasses = PictureController.class)
public class SpringWebConfiguration {
    @Bean
    RestPictureRepository restPictureRepository() {
        return new RestPictureRepository() {
            private final Map<UUID, Picture> cache = new HashMap<>();

            @Override
            public UUID save(Picture picture) {
                UUID uuid = UUID.randomUUID();
                cache.put(uuid, picture);
                return uuid;
            }

            @Override
            public Optional<Picture> fetch(UUID uuid) {
                return Optional.ofNullable(cache.get(uuid));
            }
        };
    }
}
