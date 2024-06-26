package com.pintogether.backend.config;

import com.pintogether.backend.auth.CustomAuthenticationFailureHandler;
import com.pintogether.backend.auth.JwtAuthenticationFilter;
import com.pintogether.backend.auth.OAuth2LoginSuccessHandler;
import com.pintogether.backend.auth.RestAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${frontend.url}")
    private String frontendUrl;
    @Value("${frontend.dev.url}")
    private String frontendDevUrl;

    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> {
                    auth
                            .requestMatchers(
                                    new AntPathRequestMatcher("/"),
                                    new AntPathRequestMatcher("/members/id/{member_id:\\d+}", "GET"),
                                    new AntPathRequestMatcher("/members/{member_id:\\d+}/collections/**", "GET"),
                                    new AntPathRequestMatcher("/members/{member_id:\\d+}/scraps/**", "GET"),
                                    new AntPathRequestMatcher("/members/{membername:[a-zA-Z0-9_\\$]+}", "GET"),
                                    new AntPathRequestMatcher("/collections**", "GET"),
                                    new AntPathRequestMatcher("/collections/**", "GET"),
                                    new AntPathRequestMatcher("/pins/{pin_id:\\d+}/images"),
                                    new AntPathRequestMatcher("/places**"),
                                    new AntPathRequestMatcher("/places/**"),
                                    new AntPathRequestMatcher("/search/collections**"),
                                    new AntPathRequestMatcher("/search/places**"),
                                    new AntPathRequestMatcher("/search/pins**"),
                                    new AntPathRequestMatcher("/search/members**")
                            )
                                .permitAll()
                            .anyRequest()
                                .authenticated();
                })
                .exceptionHandling(e ->
                        e.authenticationEntryPoint(restAuthenticationEntryPoint)
                )
                .addFilterAfter(
                        jwtAuthenticationFilter,
                        BasicAuthenticationFilter.class
                )
                .oauth2Login(oauth -> {
                    oauth
                            .failureHandler(customAuthenticationFailureHandler)
                            .successHandler(oAuth2LoginSuccessHandler);
                });

        return http.build();

    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(frontendUrl, frontendDevUrl));
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", configuration);
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Authorization-refresh"));

        return urlBasedCorsConfigurationSource;

    }

}
