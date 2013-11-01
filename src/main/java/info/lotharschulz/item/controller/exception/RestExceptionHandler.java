package info.lotharschulz.item.controller.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler  extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { ResourceNotFoundException.class })
    protected ResponseEntity<Object> handleMissingResource(
            final RuntimeException exception, final WebRequest request) {

        final String bodyOfResponse =
                "Resource '" + exception.getMessage() + "' does not exist?";

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);

        return handleExceptionInternal(exception, bodyOfResponse, headers,
                HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = { BadRequestException.class })
    protected ResponseEntity<Object> handleBadRequest(
            final RuntimeException exception, final WebRequest request) {

        final String bodyOfResponse =
                "Please request to an existing resource.\nResource with id '" + exception.getMessage() + "' does not exist.";

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);

        return handleExceptionInternal(exception, bodyOfResponse, headers,
                HttpStatus.BAD_REQUEST, request);
    }


}