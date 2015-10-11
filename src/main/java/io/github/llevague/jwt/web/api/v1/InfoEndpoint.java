package io.github.llevague.jwt.web.api.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.llevague.jwt.services.security.beans.UserAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

@Path("v1/info")
public class InfoEndpoint {

    @GET
    @Path("random")
    public Response random() {
        return Response.ok(Math.random()).build();
    }

    @GET
    @Path("details")
    public Response details(@Context HttpHeaders headers) throws JsonProcessingException {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof UserAuthentication) {
            UserAuthentication userAuthentication = (UserAuthentication) authentication;
            ObjectMapper mapper = new ObjectMapper();


            return Response.ok(mapper.writeValueAsString(userAuthentication.getDetails())).build();
        }
        return Response.serverError().build();
    }
}
