package com.akm.hotelmanagement.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;

public class Utils {
    public static ObjectNode emptyJson = new ObjectMapper().createObjectNode();

    public static HttpServletRequest getHttpServletRequest(WebRequest webRequest) {
        if (webRequest instanceof NativeWebRequest) {
            return ((NativeWebRequest) webRequest).getNativeRequest(HttpServletRequest.class);
        }
        return null;
    }

    public static Pageable getPageable(int page, int size, String sortBy, String sortDir) {
        return PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
    }

    public static boolean isAuthenticatedUser(String userName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails userDetails) {
                return userDetails.getUsername().equals(userName);
            }
        }
        return false;
    }
}