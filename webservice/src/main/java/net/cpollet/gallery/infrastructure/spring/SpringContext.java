package net.cpollet.gallery.infrastructure.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        SpringDataJdbcConfiguration.class,
        SpringWebConfiguration.class,
        ApplicationConfiguration.class
})
public class SpringContext {
}
