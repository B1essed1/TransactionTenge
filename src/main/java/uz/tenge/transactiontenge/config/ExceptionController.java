
package uz.tenge.transactiontenge.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;
import uz.tenge.transactiontenge.data.ResponseWrapper;
import uz.tenge.transactiontenge.exception.ExceptionWithStatusCode;

import java.util.Locale;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    private final MessageSource messageSource;

    public ExceptionController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    @ExceptionHandler({ExceptionWithStatusCode.class})
    Mono<ResponseEntity<?>> handeCustomErrors(ExceptionWithStatusCode ex, ServerHttpRequest request) {

        Locale locale = request.getHeaders()
                .getAcceptLanguageAsLocales()
                .stream()
                .findFirst()
                .orElse(null);

        String message = messageSource.getMessage(ex.getMessageKey(), null, locale);
        ResponseWrapper response = ResponseWrapper.allTypeException(message, ex.getHttpStatusCode());
        return Mono.just(ResponseEntity.status(ex.getHttpStatusCode())
                .body(response));
    }

    @ExceptionHandler({Throwable.class})
    Mono<ResponseEntity<?>> handleOtherErrors(Exception ex, ServerHttpRequest request) {
        log.error(ex.getMessage(), ex.getStackTrace());

        Locale locale = request.getHeaders()
                .getAcceptLanguageAsLocales()
                .stream()
                .findFirst()
                .orElse(Locale.ENGLISH);

        String message = messageSource.getMessage("internal.server.error", null, locale);
        ResponseWrapper  response = ResponseWrapper.internalServer(message);
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(response));
    }

}
