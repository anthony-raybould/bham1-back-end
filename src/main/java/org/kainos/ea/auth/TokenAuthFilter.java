package org.kainos.ea.auth;

import io.dropwizard.auth.AuthFilter;
import org.kainos.ea.cli.User;

import javax.annotation.Priority;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;

@Priority(1000)
public class TokenAuthFilter extends AuthFilter<String, User> {

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        String authHeader = containerRequestContext.getHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            this.authenticate(containerRequestContext, null, "BEARER");
            return;
        }

        String token = authHeader.substring("Bearer".length()).trim();

        if (!this.authenticate(containerRequestContext, token, "BEARER")) {
            throw new WebApplicationException(this.unauthorizedHandler.buildResponse(this.prefix, this.realm));
        }
    }

    public static class Builder extends AuthFilter.AuthFilterBuilder<String, User, TokenAuthFilter> {
        public Builder() {
        }

        protected TokenAuthFilter newInstance() {
            return new TokenAuthFilter();
        }
    }

}
