package com.sugandrey.SecurityBootApp.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JWTUtil {

    @Value("${jwt_secret}")
    private String secret;
    public String generateToken(final String userName) {
        final Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());
        return JWT.create()
                .withSubject("user details")
                .withClaim("userName", userName)
                .withIssuedAt(new Date())
                .withIssuer("sugrobov")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateTokenAndRetrieveClaim(final String token) throws JWTVerificationException {
        final JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                                        .withSubject("user details")
                                        .withIssuer("sugrobov")
                                        .build();
        final DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("userName").asString();
    }
}
