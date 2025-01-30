package com.akm.hotelmanagement.config.security;

import com.akm.hotelmanagement.entity.util.UserRole;
import com.akm.hotelmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;

    @Autowired
    public SecurityConfig(@Lazy UserService userService) {
        this.userService = userService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(
                                        "/",
                                        "/login",
                                        "/register",
                                        "/docs",
                                        "/health",
                                        "/logout",
                                        "/hotels/**"
                                ).permitAll()
                                .requestMatchers("/admin/**").hasRole(UserRole.ADMIN.name())
                                .requestMatchers("/hotel-admin/**").hasRole(UserRole.HOTEL_ADMIN.name())
                                .requestMatchers("/users/**").hasRole(UserRole.USER.name())
                                .anyRequest().authenticated()
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginProcessingUrl("/login")
                                .successHandler(
                                        (request, response, authentication) -> {
                                            switch (authentication.getAuthorities().toString()) {
                                                case "[ROLE_ADMIN]":
                                                    response.sendRedirect("/admin");
                                                    break;
                                                case "[ROLE_USER]":
                                                    response.sendRedirect("/users/" + authentication.getName());
                                                    break;
                                                case "[ROLE_HOTEL_ADMIN]":
                                                    response.sendRedirect("/hotel-admin");
                                                    break;
                                                default:
                                                    response.sendRedirect("/login");
                                                    break;
                                            }
                                        }
                                )
                                .failureForwardUrl("/login?error=true")
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                                .logoutSuccessUrl("/login")
                )
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(sessionManagement ->
                        sessionManagement
                                .sessionFixation().migrateSession()
                                .maximumSessions(1)
                                .maxSessionsPreventsLogin(false)
                )
                .userDetailsService(userService)
        ;
        return httpSecurity.build();
    }
}
