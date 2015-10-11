package io.github.llevague.jwt.web.conf;

import org.glassfish.jersey.servlet.ServletContainer;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.*;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class WebApp implements WebApplicationInitializer {

    public void onStartup(ServletContext context) throws ServletException {
        final AnnotationConfigWebApplicationContext root = new AnnotationConfigWebApplicationContext();
        root.register(AppContext.class);

        context.addListener(new ContextLoaderListener(root));
        context.addListener(new RequestContextListener());

        // ######### Filters
        // ## Spring security
        final FilterRegistration.Dynamic springSecurityFilterChain =
                context.addFilter("springSecurityFilterChain", DelegatingFilterProxy.class);
        springSecurityFilterChain.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");
        springSecurityFilterChain.setAsyncSupported(true);

        // ######### Servlets
        final Map<String, String> jerseyInitParams = new HashMap<String, String>() {{
            put("javax.ws.rs.Application", "io.github.llevague.jwt.web.conf.jersey.JerseyConfig");
            put("jersey.config.server.response.setStatusOverSendError", "true");
        }};
        final ServletRegistration.Dynamic jerseyServlet = context.addServlet("jerseyServlet", ServletContainer.class);
        jerseyServlet.setInitParameters(jerseyInitParams);
        jerseyServlet.addMapping("/api/*");
        jerseyServlet.setLoadOnStartup(1);

    }
}
