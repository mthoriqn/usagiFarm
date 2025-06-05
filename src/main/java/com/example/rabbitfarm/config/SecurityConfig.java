package com.example.rabbitfarm.config;

import com.example.rabbitfarm.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService)
            .passwordEncoder(passwordEncoder());

        // For initial testing, you might want an in-memory user
        // auth.inMemoryAuthentication()
        //     .withUser("user")
        //     .password(passwordEncoder().encode("password"))
        //     .roles("USER")
        //     .and()
        //     .withUser("admin")
        //     .password(passwordEncoder().encode("admin"))
        //     .roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Disable CSRF for simpler API testing (consider enabling for production with proper handling)
            .authorizeRequests()
                .antMatchers("/", "/dashboard", "/api/auth/register", "/login.html", "/index.html", "/css/**", "/js/**", "/images/**").permitAll() // Public endpoints and static resources
                .antMatchers("/api/**").authenticated() // Secure API endpoints
                // .antMatchers("/api/admin/**").hasRole("ADMIN") // Example for role-based access
                .anyRequest().authenticated() // All other requests need authentication
            .and()
            .formLogin()
                .loginPage("/login.html") // Custom login page
                .loginProcessingUrl("/login") // URL to submit the username and password to
                .defaultSuccessUrl("/", true) // Page to redirect to on successful login
                .failureUrl("/login.html?error=true") // Page to redirect to on failure
                .permitAll()
            .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login.html?logout=true")
                .permitAll();
    }
}
