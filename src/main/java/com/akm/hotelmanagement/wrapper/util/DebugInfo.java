package com.akm.hotelmanagement.wrapper.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
@AllArgsConstructor
public class DebugInfo {
    private String className;
    private String message;
    private String stackTrace;

    public DebugInfo(Exception ex) {
        this.className = ex.getClass().getSimpleName();
        this.message = ex.getMessage();
        this.stackTrace = Arrays.toString(ex.getStackTrace());
    }
}