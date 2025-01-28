package com.akm.hotelmanagement.wrapper.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDetails {
    @Nullable
    private String type; // NotFound, Conflict, Server Error, etc
    @Nullable
    private Integer status;
    @Nullable
    private String detail;
    @Nullable
    private String path;
    @Nullable
    private DebugInfo debugInfo;
    @Nullable
    private LocalDateTime timestamp;

    public ErrorDetails(@Nullable String type, @Nullable Integer status, @Nullable String detail, @Nullable String path, @Nullable DebugInfo debugInfo) {
        this.type = type;
        this.status = status;
        this.detail = detail;
        this.path = path;
        this.debugInfo = debugInfo;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorDetails(HttpStatus status, Exception ex, String path) {
        this(
                status.name(),
                status.value(),
                ex.getMessage(),
                path,
                new DebugInfo(ex)
        );
    }

    public ErrorDetails(Exception ex, String path) {
        this(
                (ex instanceof ResponseStatusException) ? ((ResponseStatusException) ex).getStatusCode().toString() : "Server Error",
                ex instanceof ResponseStatusException ? ((ResponseStatusException) ex).getStatusCode().value() : 500,
                ex.getMessage(),
                path,
                new DebugInfo(ex)
        );
    }
}
