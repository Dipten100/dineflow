package com.dineflow.dineflow_backend.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                defaultGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractCustomAuthorities(jwt).stream()
        ).collect(Collectors.toSet());

        String principalClaimName = JwtClaimNames.SUB;
        return new JwtAuthenticationToken(jwt, authorities, principalClaimName);
    }

    private Collection<GrantedAuthority> extractCustomAuthorities(Jwt jwt) {
        Collection<GrantedAuthority> authorities = new java.util.ArrayList<>();

        // Extract permissions as authorities
        Object permissionsClaim = jwt.getClaim("permissions");
        if (permissionsClaim instanceof Collection) {
            ((Collection<?>) permissionsClaim).stream()
                    .map(Object::toString)
                    .map(SimpleGrantedAuthority::new)
                    .forEach(authorities::add);
        }

        return authorities;
    }
}
