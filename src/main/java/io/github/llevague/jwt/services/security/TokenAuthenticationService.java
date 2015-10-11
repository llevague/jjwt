package io.github.llevague.jwt.services.security;

import io.github.llevague.jwt.services.security.beans.UserAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static fj.data.Option.fromNull;
import static fj.data.Option.fromString;

public class TokenAuthenticationService {
    private static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";
    private final TokenHandler tokenHandler;

    private TokenAuthenticationService(String secret, UserService userService) {
        tokenHandler = TokenHandler.of(secret, userService);
    }

    public static TokenAuthenticationService of(String secret, UserService userService) {
        return new TokenAuthenticationService(secret, userService);
    }

    public void addAuthentication(HttpServletResponse response, UserAuthentication authentication) {
        final User user = authentication.getDetails();
        response.addHeader(AUTH_HEADER_NAME, tokenHandler.createTokenForUser(user));
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        return fromString(request.getHeader(AUTH_HEADER_NAME))
                .bind(token -> fromNull(tokenHandler.parseUserFromToken(token)))
                .map(UserAuthentication::new)
                .toNull();
    }
}
