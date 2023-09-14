package org.kainos.ea.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api")
public class HelloWorldController {
    @GET
    @Path("/hello-world")
    @Produces(MediaType.APPLICATION_JSON)
    public Response helloWorld() {

        return Response.ok().entity("Hello World").build();
    }
}
