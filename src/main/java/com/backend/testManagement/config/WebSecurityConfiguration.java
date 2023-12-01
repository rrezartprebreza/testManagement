package com.backend.testManagement.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                .mvcMatchers("/swagger-ui/**", "/v3/**").permitAll()
                .and()
                .authorizeRequests().antMatchers("/api/tests/save").access("hasAnyRole('admin')")
                .and()
                .oauth2ResourceServer()
                .jwt().jwtAuthenticationConverter(jwtAuthenticationConverter());
        http.csrf().disable()
                .exceptionHandling().authenticationEntryPoint(myAuthenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler());


        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, ex) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "Access denied. You don't have permission.");

        };
    }

    @Bean
    public AuthenticationEntryPoint myAuthenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "You have no permission to make this operation. Please login with an authorized user");

        };
    }

    private void sendErrorResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setContentType("application/json");
        response.setStatus(statusCode);
        response.getOutputStream().println(
                "{ \"timestamp\" : \"" + new Timestamp(new Date().getTime()) + "\"," +
                        "\n \"message\" : \"" + message + "\"," +
                        "\n \"status\" : " + response.getStatus() + "}");
    }

    private Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());

        return jwtAuthenticationConverter;
    }

    private static class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
        private Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        @SuppressWarnings("unchecked")
        @Override
        public Collection<GrantedAuthority> convert(Jwt jwt) {

            final Map<String, Object> resourceAccess = (Map<String, Object>) jwt.getClaims().get("resource_access");

            if (resourceAccess != null) {

                final Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get("rest-api");

                if (clientAccess != null) {

                    Object rolesObj = clientAccess.get("roles");

                    if (rolesObj instanceof List) {

                        List<String> roles = (List<String>) rolesObj;

                        grantedAuthorities = roles.stream()
                                .map(roleName -> "ROLE_" + roleName)
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList());

                    }

                }

            }

            return grantedAuthorities;

        }

    }
}

