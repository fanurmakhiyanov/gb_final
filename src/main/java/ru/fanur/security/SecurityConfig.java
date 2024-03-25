package ru.fanur.security;

import ru.fanur.advice.ExceptionControllerAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebMvc
public class SecurityConfig {
    @Autowired
    private JwtFilter jwtFilter;
    @Autowired
    private ExceptionControllerAdvice exceptionControllerAdvice;

    private static final String AUTHENTICATION_ENTRY_POINT = "/login";

    @Value("${security.access.allowed_methods}")
    private String allowedMethods;
    @Value("${security.access.allowed_headers}")
    private String allowedHeaders;
    @Value("${security.access.outer_source.url}")
    private String allowedOuterSource;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> {
                    request.requestMatchers(AUTHENTICATION_ENTRY_POINT).permitAll()
                            .anyRequest().authenticated();
                })
                .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(cors -> cors.configurationSource(myWebsiteConfigurationSource()))
                .exceptionHandling((handler) -> {
                    handler.authenticationEntryPoint(exceptionControllerAdvice);
                })
                .build();
    }

    @Bean
    CorsConfigurationSource myWebsiteConfigurationSource() {
        var configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Collections.singletonList(allowedOuterSource));
        configuration.setAllowedMethods(List.of(allowedMethods));
        configuration.setAllowedHeaders(List.of(allowedHeaders));
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}