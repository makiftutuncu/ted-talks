package dev.akif.tedtalks.errors;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class Errors {
    public static boolean isDuplicateTedTalkError(@NonNull Throwable t) {
        if (t instanceof DuplicateTedTalk) {
            return true;
        }

        val cause = NestedExceptionUtils.getRootCause(t);
        val message = cause != null ? cause.getMessage() : t.getMessage();
        return message.contains("duplicate key value violates unique constraint") && message.contains("ted_talks_author_id_title_key");
    }

    @ExceptionHandler(DuplicateTedTalk.class)
    public static ResponseEntity<String> handleDuplicateTedTalk(@NonNull DuplicateTedTalk e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .contentType(MediaType.TEXT_PLAIN)
                .body("TED talk with title '%s' and author '%s' already exists".formatted(e.title, e.author));
    }

    @ExceptionHandler(TedTalkNotFound.class)
    public static ResponseEntity<String> handleTedTalkNotFound(@NonNull TedTalkNotFound e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.TEXT_PLAIN)
                .body("TED talk with id '%d' is not found".formatted(e.id));
    }

    @ExceptionHandler(Exception.class)
    public static ResponseEntity<String> handleException(@NonNull Exception e) {
        log.error("An unknown error occurred", e);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.TEXT_PLAIN)
                .body("An unknown error occurred");
    }
}
