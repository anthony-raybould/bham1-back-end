package org.kainos.ea.api;

import org.kainos.ea.cli.Login;
import org.kainos.ea.client.FailedToGenerateTokenException;
import org.kainos.ea.client.FailedToLoginException;
import org.kainos.ea.db.AuthDao;

import java.sql.SQLException;

public class AuthService {
    private AuthDao authDao;

    public AuthService(AuthDao authDao) {
        if(authDao == null)
        {
            throw new NullPointerException("AuthDao service provided is null");
        }
        this.authDao = authDao;
    }

    public String login(Login login) throws FailedToLoginException, FailedToGenerateTokenException {
                if (authDao.validLogin(login)) {
                    try {
                return authDao.generateToken(login.getEmail());
            } catch (SQLException e) {
                throw new FailedToGenerateTokenException();
            }
        }

        throw new FailedToLoginException();
    }

}
