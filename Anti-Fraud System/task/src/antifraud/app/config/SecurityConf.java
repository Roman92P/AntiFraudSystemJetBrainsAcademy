package antifraud.app.config;

import antifraud.app.exception.RestAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Roman Pashkov created on 26.08.2022 inside the package - antifraud.app.config
 */
@Configuration
@EnableWebSecurity
public class SecurityConf extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .authenticationEntryPoint(restAuthenticationEntryPoint) // Handles auth error
                .and()
                .csrf().disable().headers().frameOptions().disable() // for Postman, the H2 console
                .and()
                .authorizeRequests() // manage access
                .antMatchers(HttpMethod.POST, "/api/auth/user").permitAll()
                .antMatchers(HttpMethod.DELETE, "/api/auth/user/**").hasAuthority("ROLE_ADMINISTRATOR")
                .antMatchers("/actuator/shutdown").permitAll() // needs to run test
                // other matchers
                .antMatchers(HttpMethod.PUT, "/api/antifraud/transaction").hasAuthority("ROLE_SUPPORT")
                .antMatchers(HttpMethod.POST,"/api/antifraud/transaction/**").hasAuthority("ROLE_MERCHANT")
                .antMatchers("/api/auth/list").hasAnyAuthority("ROLE_ADMINISTRATOR","ROLE_SUPPORT")
                .antMatchers("/api/auth/user/{username}").authenticated()
                .antMatchers("/api/auth/role/**").hasAuthority("ROLE_ADMINISTRATOR")
                .antMatchers("/api/auth/access/**").hasAuthority("ROLE_ADMINISTRATOR")
                .antMatchers("/api/antifraud/suspicious-ip/**").hasAuthority("ROLE_SUPPORT")
                .antMatchers("/api/antifraud/stolencard/**").hasAuthority("ROLE_SUPPORT")
                .antMatchers("/api/antifraud/history/**").hasAuthority("ROLE_SUPPORT")
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // no session
    }
}