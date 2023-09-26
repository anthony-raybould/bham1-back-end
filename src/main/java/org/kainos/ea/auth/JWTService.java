package org.kainos.ea.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

public class JWTService {

    private static final String JWT_SECRET = "superSecretWords";

    public String create(String email, int userId) {
        Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);

        return JWT.create().withExpiresAt(DateUtils.addDays(new Date(), 1))
                .withClaim("userId", userId)
                .withClaim("email", email)
                .withIssuer("auth0")
                .sign(algorithm);
    }

    public Integer verify(String token) {
        Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
        return JWT.require(algorithm)
                .withIssuer("auth0")
                .build()
                .verify(token)
                .getClaim("userId")
                .asInt();
    }
}
