package net.cpollet.gallery.infrastructure.spring;

import net.cpollet.gallery.infrastructure.AWTPhysicalImageFactory;
import net.cpollet.gallery.infrastructure.PhysicalImageFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        SpringDataJdbcConfiguration.class,
        SpringWebConfiguration.class,
        ApplicationConfiguration.class
})
public class SpringContext {
    @Bean
    PhysicalImageFactory physicalImageFactory() {
        return new AWTPhysicalImageFactory();
    }
}
