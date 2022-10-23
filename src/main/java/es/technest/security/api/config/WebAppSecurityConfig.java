package es.technest.security.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {

    private final ActuatorUserDetailsService userDetailsService;

    private final CorrelationId correlationId;

    @Autowired
    public WebAppSecurityConfig(ActuatorUserDetailsService userDetailsService,
                                CorrelationId correlationId) {
        this.userDetailsService = userDetailsService;
        this.correlationId = correlationId;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        configureApplication(http);
        configureActuator(http);
    }

    private void configureActuator(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/actuator/account/**").hasRole("UPDATE_ACCOUNT_PERMISSION")
                .anyRequest().permitAll()
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
    }

    private void configureApplication(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterAfter(new CorrelationIdFilter(correlationId), BasicAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


