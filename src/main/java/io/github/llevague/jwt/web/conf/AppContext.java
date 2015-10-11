package io.github.llevague.jwt.web.conf;

import io.github.llevague.jwt.services.conf.SecurityConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({SecurityConfig.class})
public class AppContext {

}
