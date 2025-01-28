package com.akm.hotelmanagement.wrapper.util;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

/**
 * A class to hold metadata about the response.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Meta {
    private String timestamp;
    private String apiVersion;
    @Nullable
    private String requestId;
    @Nullable
    private String path;
    private String server;
    private String environment;

    public Meta(@Nullable String requestId, @Nullable String processTime) { // todo: change hardcoded values here
        this(
                LocalDateTime.now().toString(),
                "1.0",
                requestId,
                processTime,
                "localhost",
                "dev"
        );
    }

    public Meta(HttpServletRequest request) {
        this.timestamp = LocalDateTime.now().toString();
        this.apiVersion = "1.0";
        this.requestId = (request == null) ? null : request.getRequestId();
        this.path = (request == null) ? null : request.getPathInfo();
        this.server = "localhost";
        this.environment = "dev";
    }

}