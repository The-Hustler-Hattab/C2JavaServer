package com.mtattab.c2cServer.config;

import com.mtattab.c2cServer.filters.CsrfCookieFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import static com.mtattab.c2cServer.util.Constants.*;

@Configuration
public class ProjectSecurityConfig {
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler requestAttributeHandler= new CsrfTokenRequestAttributeHandler();
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        requestAttributeHandler.setCsrfRequestAttributeName("_csrf");

        http
//                session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                path permissions
                .authorizeRequests()


//                .anyRequest().permitAll()
//                .and()
//                .csrf()
//                .disable()

                .requestMatchers("/swagger-ui/**","/v3/api-docs/**").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers(S3_PATH+S3_UPLOAD).permitAll()
                .requestMatchers(WEBSOCKET_REVERSE_SHELL).permitAll()
                .requestMatchers(WEBSOCKET_REVERSE_SHELL_MANAGER).permitAll()
                .anyRequest()
                .authenticated()
//                .permitAll()
                .and()

//                csrf
                .csrf((csrf) -> csrf.csrfTokenRequestHandler(requestAttributeHandler)
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers(S3_PATH+S3_UPLOAD)
                )


                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
//              cors
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//              resource server
                .oauth2ResourceServer(oauth2ResourceServerCustomizer ->
                oauth2ResourceServerCustomizer.jwt(jwtCustomizer -> jwtCustomizer.jwtAuthenticationConverter(jwtAuthenticationConverter)))


        ;

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.applyPermitDefaultValues(); // This allows all origins, methods, and headers. Customize as needed.

        corsConfig.addAllowedOrigin("http://localhost:4200");
        corsConfig.addAllowedOrigin("https://c2.mtattab.com");

        corsConfig.addAllowedMethod("*");
        corsConfig.addAllowedHeader("*"); // You might want to restrict headers

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig); // This means CORS will apply to all endpoints.

        return source;
    }
}
