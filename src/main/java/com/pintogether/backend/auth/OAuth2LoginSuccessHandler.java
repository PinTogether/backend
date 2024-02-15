package com.pintogether.backend.auth;

import com.pintogether.backend.domain.User;
import com.pintogether.backend.domain.enums.RegistrationSource;
import com.pintogether.backend.repository.UserRepository;
import com.pintogether.backend.util.RandomNicknameGenerator;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;
    @Value("${jwt.signing.key}")
    private String signingKey;
    @Value("${frontend.url}")
    private String frontendUrl;
    @Value("${frontend.cookie.url}")
    private String cookieUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        String registrationKey="";
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = principal.getAttributes();

        if ("google".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
            registrationKey = attributes.getOrDefault("email", "").toString();
        } else if ("naver".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
            Map<String, Object> responseNaver = (Map<String, Object>) attributes.get("response");
            registrationKey = responseNaver.getOrDefault("id", "").toString();
            String name = responseNaver.getOrDefault("name", "").toString();
            System.out.println("registrationKey : " + registrationKey);
        } else if ("kakao".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
            registrationKey = attributes.getOrDefault("id", "").toString();
        }

        Optional<User> userOptional = userRepository.findByRegistrationKey(registrationKey);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            sendJwtByCookie(user, registrationKey, response);
        } else {
            String newNickname = RandomNicknameGenerator.generateNickname();
            User user = User.builder()
                    .nickname(newNickname)
                    .registrationSource(RegistrationSource.valueOf(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId().toUpperCase()))
                    .registrationPk(registrationKey)
                    .build();
            userRepository.save(user);
            sendJwtByCookie(user, registrationKey, response);

        }
        this.setAlwaysUseDefaultTargetUrl(true);
        this.setDefaultTargetUrl(frontendUrl);
        super.onAuthenticationSuccess(request, response, authentication);

    }

    public void sendJwtByCookie(User user, String registrationKey, HttpServletResponse response) {
        SecretKey key = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));
        String jwt = Jwts.builder()
                .setClaims(Map.of("id", user.getId(), "registrationKey", registrationKey, "Role", "Role_USER"))
                .signWith(key)
                .compact();
        ResponseCookie cookie = ResponseCookie
                .from("Authorization", jwt)
                .domain(cookieUrl)
                .path("/")
                .httpOnly(false)
                .secure(true)
                .maxAge(Duration.ofDays(30))
                .sameSite("None")
                .build();

        response.setHeader("Set-Cookie", cookie.toString());
        System.out.println("========Sended Cookie ==========");
    }
}

