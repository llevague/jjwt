package io.github.llevague.jwt.services.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;
import org.springframework.security.core.userdetails.User;

import static fj.data.Option.fromString;

@Value(staticConstructor = "of")
@Getter(AccessLevel.NONE)
public class TokenHandler {
    private String secret;
    private UserService userService;

    public User parseUserFromToken(String token) {
        String username = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return userService.loadUserByUsername(username);
    }

    public String createTokenForUser(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public static void main(String[] args) {
        String token = Jwts.builder()
                .setSubject(fromString(args[0]).orSome(() -> ""))
                .signWith(SignatureAlgorithm.HS512, fromString(args[1]).orSome(() -> ""))
                .compact();
        System.out.println(token);
    }
}
