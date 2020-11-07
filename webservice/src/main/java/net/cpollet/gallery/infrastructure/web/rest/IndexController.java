package net.cpollet.gallery.infrastructure.web.rest;

import lombok.extern.slf4j.Slf4j;
import net.cpollet.gallery.infrastructure.web.rest.data.Index;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
@Slf4j
public class IndexController {
    @GetMapping("/")
    public Index index() {
        return new Index();
    }
}
