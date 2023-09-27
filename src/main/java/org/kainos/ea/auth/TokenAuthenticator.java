package org.kainos.ea.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import org.kainos.ea.cli.User;
import org.kainos.ea.client.FailedToValidateTokenException;

import java.util.Optional;

public class TokenAuthenticator implements Authenticator<String, User> {

    private final TokenService tokenService;

    public TokenAuthenticator(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public Optional<User> authenticate(String token) throws AuthenticationException {
        if (token == null) {
            return Optional.empty();
        }

        try {
            User user = tokenService.validateToken(token);
            if (user != null) {
                return Optional.of(user);
            }
        } catch (FailedToValidateTokenException e) {
            throw new AuthenticationException(e);
        }

        return Optional.empty();
    }

}
