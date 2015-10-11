package io.github.llevague.jwt.services.conf;

import io.github.llevague.jwt.services.security.TokenAuthenticationService;
import io.github.llevague.jwt.services.security.UserService;
import io.github.llevague.jwt.services.security.filters.StatelessAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final TokenAuthenticationService tokenAuthenticationService;

    //TODO: to externalize
    private static final String secret = "tooManySecrets";

    public SecurityConfig() {
        super(true);
        this.userService = new UserService();
        tokenAuthenticationService = TokenAuthenticationService.of(secret, userService);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/swagger.json");
        super.configure(web);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling()
                .and()
                .authorizeRequests()

                // All other request need to be authenticated
                .anyRequest().authenticated().and()

                // Custom Token based authentication based on the header previously given to the client
                .addFilterBefore(StatelessAuthenticationFilter.of(tokenAuthenticationService),
                        UsernamePasswordAuthenticationFilter.class);
    }



    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    @Override
    public UserService userDetailsService() {
        return userService;
    }

    @Bean
    public TokenAuthenticationService tokenAuthenticationService() {
        return tokenAuthenticationService;
    }
}
