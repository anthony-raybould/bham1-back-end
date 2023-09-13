package org.kainos.ea.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import org.kainos.ea.cli.User;
import org.kainos.ea.db.AuthDao;

import java.sql.SQLException;
import java.util.Optional;

public class TokenAuthenticator implements Authenticator<String, User> {

    private final AuthDao authDao;

    public TokenAuthenticator(AuthDao authDao) {
        this.authDao = authDao;
    }

    @Override
    public Optional<User> authenticate(String token) throws AuthenticationException {
        if (token == null) {
            return Optional.empty();
        }

        try {
            User user = authDao.validateToken(token);
            if (user != null) {
                return Optional.of(user);
            }
        } catch (SQLException e) {
            throw new AuthenticationException(e);
        }

        return Optional.empty();
    }

}
