package com.akm.hotelmanagement.wrapper;

import com.akm.hotelmanagement.wrapper.util.ErrorDetails;
import com.akm.hotelmanagement.wrapper.util.Meta;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
public class ResponseWrapper<T> { // todo: make this extend from ResponseEntity<T> and then directly return it from controller
    private boolean status;
    private int code;
    private String message;
    private T data;
    private ErrorDetails error;
    private Meta meta;

    public ResponseWrapper(boolean status, int code, String message, T data, ErrorDetails error, HttpServletRequest request) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
        this.error = error;
        this.meta = new Meta(request);
    }

    public static <T> ResponseWrapper<T> getResponseWrapper(boolean status, int code, String message, T data, ErrorDetails error, HttpServletRequest request) {
        return new ResponseWrapper<>(
                status,
                code,
                message,
                data,
                error,
                request
        );
    }

    public static <T> ResponseWrapper<T> getSuccessResponseWrapper(int code, String message, T data, HttpServletRequest request) {
        return getResponseWrapper(
                true,
                code,
                message,
                data,
                new ErrorDetails(),
                request
        );
    }

    public static <T> ResponseWrapper<T> getErrorResponseWrapper(int code, String message, ErrorDetails error, HttpServletRequest request) {
        return getResponseWrapper(
                false,
                code,
                message,
                null,
                error,
                request
        );
    }

    public static <T> ResponseWrapper<T> getOkResponseWrapper(T data, HttpServletRequest request) {
        return getSuccessResponseWrapper(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                data,
                request
        );
    }

    public static <T> ResponseWrapper<T> getCreatedResponseWrapper(T data, HttpServletRequest request) {
        return getSuccessResponseWrapper(
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(),
                data,
                request
        );
    }

    public static <T> ResponseWrapper<PagedResponse<T>> getOkResponseWrapperPaged(Page<T> data, List<T> items, HttpServletRequest request) {
        return getSuccessResponseWrapper(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                new PagedResponse<>(data, items),
                request
        );
    }

    public static <T> ResponseWrapper<PagedResponse<T>> getOkResponseWrapperPaged(Page<T> data, HttpServletRequest request) {
        return getOkResponseWrapperPaged(
                data,
                data.getContent(),
                request
        );
    }

    public static <T> ResponseWrapper<T> getNoContentResponseWrapper(HttpServletRequest request) {
        return getSuccessResponseWrapper(
                HttpStatus.NO_CONTENT.value(),
                HttpStatus.NO_CONTENT.getReasonPhrase(),
                null,
                request
        );
    }
}