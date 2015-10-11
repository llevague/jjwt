package io.github.llevague.jwt.web.conf.jersey;

import io.github.llevague.jwt.web.api.v1.InfoEndpoint;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        // Enable Spring and Jackson support
        register(RequestContextFilter.class);
        register(JacksonObjectMapperConfig.class);

        // Application resources
        register(InfoEndpoint.class);
    }
}
