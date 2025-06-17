
package uz.tenge.transactiontenge.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;
import uz.tenge.transactiontenge.exception.ExceptionWithStatusCode;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

   // private final LocalizationService service;

//    public ExceptionController(LocalizationService service) {
//        this.service = service;
//    }

    @ExceptionHandler({ExceptionWithStatusCode.class})
    Mono<ResponseEntity<?>> handeCustomErrors(ExceptionWithStatusCode ex) {
        //TODO  internationalization
        log.error(ex.getMessage(), ex.getStackTrace());

        // String message  = service.getMessage(ex.getMessageKey());
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Resource not found: " + ex.getMessage()));
    }

    @ExceptionHandler({Throwable.class})
    Mono<ResponseEntity<?>> handleOtherErrors(Exception ex) {
        log.error(ex.getMessage(), ex.getStackTrace());

        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Resource not found: " + ex.getMessage()));
    }

}
