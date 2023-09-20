package org.kainos.ea.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JWTServiceTest {
    JWTService jwtService = new JWTService();
    @Test
    public void create_WhenValidInputs_shouldReturnValidJWT()
    {
        String email = "email@fakeEmail.com";
        String jwt = jwtService.create(email, 1);
        DecodedJWT decodedJWT = JWT.decode(jwt);
        assertEquals(decodedJWT.getClaim("email").asString().equals(email), true);
        assertSame(decodedJWT.getClaim("userId").asInt(), 1);
        assertNotNull(decodedJWT.getClaim("expiry"));
    }

    @Test
    public void verify_WhenValidJWT_shouldReturnUserId() {
        String email = "email@fakeEmail.com";
        String jwt = jwtService.create(email, 1);
        int userId = jwtService.verify(jwt);
        assertSame(userId, 1);
    }
}
