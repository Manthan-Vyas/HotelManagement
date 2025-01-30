package com.akm.hotelmanagement.exception;

import com.akm.hotelmanagement.wrapper.ResponseWrapper;
import com.akm.hotelmanagement.wrapper.util.DebugInfo;
import com.akm.hotelmanagement.wrapper.util.ErrorDetails;
import jakarta.validation.ValidationException;
import lombok.NonNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.StringJoiner;

import static com.akm.hotelmanagement.util.Utils.getHttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ResponseWrapper<Object>> handleAllExceptions(Exception ex, WebRequest request) {
        HttpStatus status = getHttpStatusFromException(ex);
        ErrorDetails errorDetails = new ErrorDetails(
                ex,
                request.getDescription(false)
        );
        return ResponseEntity.status(status).body(
                ResponseWrapper.getErrorResponseWrapper(
                        status.value(),
                        status.getReasonPhrase(),
                        errorDetails,
                        getHttpServletRequest(request)
                )
        );
    }

    private static HttpStatus getHttpStatusFromException(Exception ex) {
        if (ex instanceof ValidationException || ex instanceof IllegalArgumentException) {
            return HttpStatus.BAD_REQUEST;
        } else if (ex instanceof AuthenticationException) {
            return HttpStatus.UNAUTHORIZED;
        } else if (ex instanceof ErrorResponseException) {
            return HttpStatus.valueOf(((ErrorResponseException) ex).getStatusCode().value());
        } else if (ex instanceof AccessDeniedException) {
            return HttpStatus.FORBIDDEN;
        } else if (ex instanceof RestClientResponseException) {
            return HttpStatus.valueOf(((RestClientResponseException) ex).getStatusCode().value());
        } else if (ex instanceof HotelManagementApiException) {
            return ((HotelManagementApiException) ex).httpStatus;
        } else if (ex instanceof DataIntegrityViolationException) {
            return HttpStatus.CONFLICT;
        } else if (ex instanceof NoResourceFoundException) {
            return HttpStatus.NOT_FOUND;
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@NonNull HttpMessageNotReadableException ex, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                "Malformed JSON Request",
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getDescription(false),
                new DebugInfo(ex)
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ResponseWrapper.getErrorResponseWrapper(
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        errorDetails,
                        getHttpServletRequest(request)
                )
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        StringJoiner errorMessageJoiner = new StringJoiner(", ", "Validation Failed due to " + ex.getErrorCount() + " following errors: ", ".");
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> errorMessageJoiner.add(fieldError.getDefaultMessage()));
        ex.getBindingResult().getGlobalErrors().forEach(globalError -> errorMessageJoiner.add(globalError.getDefaultMessage()));

        String errorMessageStr = errorMessageJoiner.toString();

        ErrorDetails errorDetails = new ErrorDetails(
                "Validation Error",
                HttpStatus.BAD_REQUEST.value(),
                errorMessageStr,
                request.getDescription(false),
                new DebugInfo(ex)
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ResponseWrapper.getErrorResponseWrapper(
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        errorDetails,
                        getHttpServletRequest(request)
                )
        );
    }
}
