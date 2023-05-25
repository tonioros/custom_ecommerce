package org.antonioxocoy.cecommerce.config;

import org.antonioxocoy.cecommerce.jwt.AuthEntryPointJWT;
import org.antonioxocoy.cecommerce.jwt.JWTAuthenticationFilter;
import org.antonioxocoy.cecommerce.jwt.JWTAuthorizationTokenFilter;
import org.antonioxocoy.cecommerce.jwt.JWTTokenUtil;
import org.antonioxocoy.cecommerce.jwt.services.UserDetailServiceImpl;
import org.antonioxocoy.cecommerce.util.SecureUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.security.SecureRandom;

import static org.antonioxocoy.cecommerce.util.SecureUtil.BCRYPT_STENGETH;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private UserDetailServiceImpl userDetailService;
    @Autowired
    private JWTAuthorizationTokenFilter authorizationTFilter;
    @Autowired
    private JWTTokenUtil tokenUtil;
    @Bean
    SecurityFilterChain web(HttpSecurity http, AuthenticationManager manager) throws Exception {
        JWTAuthenticationFilter authenticationFilter = new JWTAuthenticationFilter(tokenUtil);
        authenticationFilter.setAuthenticationManager(manager);
        authenticationFilter.setFilterProcessesUrl("/api/global/login");

        http
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeHttpRequests((authorize) -> {
                            try {
                                authorize
                                        // All of 'global' APIs will be without login auth
                                        .requestMatchers("/api/global/**")
                                        .permitAll()
                                        // All of 'auth' APIs will be with login auth
                                        .requestMatchers("/api/auth/**")
                                        .authenticated()
                                        // In case, any request match with the rules before declarated, throw and bye :)
                                        .anyRequest()
                                        .denyAll()
                                        .and()
                                        .addFilter(authenticationFilter)
                                        .addFilterBefore(authorizationTFilter, UsernamePasswordAuthenticationFilter.class)
                                        .sessionManagement()
                                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                            } catch (Exception e) {
                                throw new RuntimeException(e);

                            }
                        }
                )
                .exceptionHandling(customizer -> customizer
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));
        return http.build();
    }


    @Bean
    AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
       return new BCryptPasswordEncoder();
    }
}
