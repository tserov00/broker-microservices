package org.example.gatewayservice.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class JwtToUserPrincipalConverter implements Converter<Jwt, Authentication> {

    @Override
    public Authentication convert(Jwt jwt) {
        Long userId = jwt.getClaim("userId");

        if (userId == null) {
            throw new IllegalArgumentException("Claim 'userId' is missing in JWT");
        }

        UserPrincipal principal = new UserPrincipal(userId);

        return new UsernamePasswordAuthenticationToken(
                principal,
                null,
                principal.getAuthorities()
        );
    }
}
