package org.kainos.ea.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kainos.ea.client.FailedToGetUserId;
import org.kainos.ea.db.AuthDao;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    AuthDao authDao = Mockito.mock(AuthDao.class);
    TokenService tokenService = new TokenService(authDao);

    @Test
    public void constructor_whenNullAuthDaoService_expectThrowNullPointer(){
        AuthDao nullAuthDao = null;

        assertThrows(NullPointerException.class,
                () -> new TokenService(nullAuthDao));
    }

    @Test
    public void generateToken_whenGetUserIdReturnNeg1_shouldThrowFailedToGetUserId() throws SQLException {
        Mockito.when(authDao.getUserId(any(String.class))).thenReturn(-1);

        assertThrows(FailedToGetUserId.class,
                () -> tokenService.generateToken("email"));
    }
}
