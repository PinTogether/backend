package com.pintogether.backend.config;

import com.pintogether.backend.auth.JwtAuthenticationFilter;
import com.pintogether.backend.auth.OAuth2LoginSuccessHandler;
import com.pintogether.backend.auth.RestAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${frontend.url}")
    private String frontendUrl;

    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> {
                    auth
                            .requestMatchers(
                                    new AntPathRequestMatcher("/"),
                                    new AntPathRequestMatcher("/members/{member_id:\\d+}", "GET"),
                                    new AntPathRequestMatcher("/members/{member_id:\\d+}/collections/**", "GET"),
                                    new AntPathRequestMatcher("/members/{member_id:\\d+}/scraps/**", "GET"),
                                    new AntPathRequestMatcher("/collections/{\\d+}"),
                                    new AntPathRequestMatcher("/collections/top"),
                                    new AntPathRequestMatcher("/collections/{\\d+}/pins"),
                                    new AntPathRequestMatcher("/collections/{\\d+}/comments"),
                                    new AntPathRequestMatcher("/pins/{pin_id}/images"),
                                    new AntPathRequestMatcher("/places"),
                                    new AntPathRequestMatcher("/places/{\\d+}/pins"),
                                    new AntPathRequestMatcher("/places/{place_id}"),
                                    new AntPathRequestMatcher("/search/**")
                            )
                                .permitAll()
                            .anyRequest()
                                .authenticated();
                })
                .addFilterAfter(
                        jwtAuthenticationFilter,
                        BasicAuthenticationFilter.class
                )
                .exceptionHandling(e ->
                        e.authenticationEntryPoint(new RestAuthenticationEntryPoint())
                )
                .oauth2Login(oauth -> {
                    oauth
                            .successHandler(oAuth2LoginSuccessHandler);
                });

        return http.build();

    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(frontendUrl));
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", configuration);
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Authorization-refresh"));

        return urlBasedCorsConfigurationSource;

    }

}
