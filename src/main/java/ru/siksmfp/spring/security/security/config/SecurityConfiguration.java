package ru.siksmfp.spring.security.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.siksmfp.spring.security.security.service.SecurityUserService;

/**
 * @author Artem Karnov @date 5/4/2018.
 * @email artem.karnov@t-systems.com
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private SecurityUserService securityUserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/find").access("hasRole('USER') or hasRole('ADMIN') or hasRole('ROOT')")
                .antMatchers("/delete").access("hasRole('ADMIN') or hasRole('ROOT')")
                .antMatchers("/save").access("hasRole('ROOT')")
                .and()
                .formLogin().loginPage("/login").loginProcessingUrl("/login").usernameParameter("email").passwordParameter("password")
                .and()
                .csrf()
                .and().exceptionHandling().accessDeniedPage("/error");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return String.valueOf(charSequence);
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return s.equals(String.valueOf(charSequence));
            }
        };
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(securityUserService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationTrustResolver authenticationTrustResolver() {
        return new AuthenticationTrustResolverImpl();
    }
}
