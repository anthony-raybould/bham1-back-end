package org.kainos.ea.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kainos.ea.client.FailedToGenerateTokenException;
import org.kainos.ea.client.FailedToGetUserId;
import org.kainos.ea.db.AuthDao;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    AuthDao authDao = Mockito.mock(AuthDao.class);
    JWTService jwtService = Mockito.mock(JWTService.class);
    TokenService tokenService = new TokenService(authDao, jwtService);

    @Test
    public void constructor_whenNullAuthDaoService_expectThrowNullPointer(){
        AuthDao nullAuthDao = null;

        assertThrows(NullPointerException.class,
                () -> new TokenService(nullAuthDao,jwtService));
    }
    @Test
    public void constructor_whenNullJWTService_expectThrowNullPointer(){
        JWTService nullJWTService = null;

        assertThrows(NullPointerException.class,
                () -> new TokenService(authDao,nullJWTService));
    }

    @Test
    public void generateToken_whenGetUserIdReturnNeg1_shouldThrowFailedToGetUserId() throws SQLException {
        Mockito.when(authDao.getUserId(any(String.class))).thenReturn(-1);

        assertThrows(FailedToGetUserId.class,
                () -> tokenService.generateToken("email"));
    }

    @Test
    public void generateToken_whenJWTTokenServiceThrowError_shouldCatchAndThrowFailedToGenerateToken()
    {
        Mockito.when(jwtService.create(any(String.class), any(int.class))).thenThrow(JWTCreationException.class);

        assertThrows(FailedToGenerateTokenException.class,
                () -> tokenService.generateToken("email"));
    }

    @Test
    public void generateToken_whenSuccess_shouldReturnValidJWT() throws SQLException, FailedToGetUserId, FailedToGenerateTokenException {
        JWTService unMockedJwtService = new JWTService();
        TokenService unMockedtokenService = new TokenService(authDao, unMockedJwtService);

        Mockito.when(authDao.getUserId(any(String.class))).thenReturn(1);
        String token = unMockedtokenService.generateToken("email@email.com");
        DecodedJWT decodedJWT = JWT.decode(token);
        assertEquals(decodedJWT.getClaim("email").asString().equals("email@email.com"), true);
        assertSame(decodedJWT.getClaim("userId").asInt(), 1);
        assertNotNull(decodedJWT.getClaim("expiry"));
    }
}

