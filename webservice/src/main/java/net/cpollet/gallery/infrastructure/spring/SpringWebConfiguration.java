package net.cpollet.gallery.infrastructure.spring;


import net.cpollet.gallery.infrastructure.web.rest.PictureController;
import net.cpollet.gallery.infrastructure.web.rest.RestPictureRepository;
import net.cpollet.gallery.infrastructure.web.rest.requests.CreatePictureRequest;
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
            private final Map<UUID, CreatePictureRequest> cache = new HashMap<>();

            @Override
            public UUID push(CreatePictureRequest picture) {
                UUID uuid = UUID.randomUUID();
                cache.put(uuid, picture);
                return uuid;
            }

            @Override
            public Optional<CreatePictureRequest> pull(UUID uuid) {
                return Optional.ofNullable(cache.remove(uuid));
            }
        };
    }
}
