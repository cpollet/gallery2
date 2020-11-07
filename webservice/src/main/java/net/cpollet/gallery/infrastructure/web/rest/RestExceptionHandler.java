package net.cpollet.gallery.infrastructure.web.rest;

import lombok.extern.slf4j.Slf4j;
import net.cpollet.gallery.domain.picture.errors.DomainError;
import net.cpollet.gallery.domain.picture.errors.FormatNotSupported;
import net.cpollet.gallery.domain.picture.exceptions.DomainException;
import net.cpollet.gallery.infrastructure.exceptions.InfrastructureException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(DomainException.class)
    protected ResponseEntity<Object> handleDomainException(DomainException e) {
        log.error("Caught exception from domain error [{}] with message [{}]",
                e.getDomainError().orElse(null), e.getMessage(), e);

        return e.getDomainError()
                .map(this::mapDomainError)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    private ResponseEntity<Object> mapDomainError(DomainError domainError) {
        return Match(domainError).of(
                Case($(instanceOf(FormatNotSupported.class)), ResponseEntity.badRequest().build()),
                Case($(), ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build())
        );
    }

    @ExceptionHandler(InfrastructureException.class)
    protected ResponseEntity<Object> handleDomainException(InfrastructureException e) {
        log.error("Caught infrastructure exception with message [{}]", e.getMessage(), e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
