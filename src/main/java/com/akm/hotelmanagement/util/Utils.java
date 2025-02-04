package com.akm.hotelmanagement.util;

import com.akm.hotelmanagement.entity.util.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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

    public static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(a -> a.equals(role));
        }
        return false;
    }

    public static UserRole getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {

            UserRole userRole = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .map(userRoleString -> userRoleString.substring(5)) // todo: improve this
                    .map(UserRole::valueOf)
                    .findFirst()
                    .orElse(UserRole.GUEST);
            return userRole;
        }
        return UserRole.GUEST;
    }

    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails userDetails) {
                return userDetails.getUsername();
            }
        }
        return null;
    }

    public static boolean isAuthenticatedUser(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails userDetails) {
                return userDetails.getUsername().equals(username);
            }
        }
        return false;
    }
}