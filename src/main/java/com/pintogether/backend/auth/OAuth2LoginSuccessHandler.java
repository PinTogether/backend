package com.pintogether.backend.auth;

import com.pintogether.backend.entity.Member;
import com.pintogether.backend.entity.enums.RegistrationSource;
import com.pintogether.backend.entity.enums.RoleType;
import com.pintogether.backend.repository.MemberRepository;
import com.pintogether.backend.service.MemberService;
import com.pintogether.backend.util.RandomNicknameGenerator;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
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

    private static final Logger logger = LoggerFactory.getLogger(OAuth2LoginSuccessHandler.class);
    private final MemberService memberService;
    @Value("${jwt.signing.key}")
    private String signingKey;
    @Value("${frontend.login.success.url}")
    private String frontendUrl;
    @Value("${frontend.cookie.url}")
    private String cookieUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        logger.info(authentication.getName() + " is trying to login");
        String registrationId="";
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = principal.getAttributes();

        if ("google".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
            registrationId = attributes.getOrDefault("email", "").toString();
        } else if ("naver".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
            Map<String, Object> responseNaver = (Map<String, Object>) attributes.get("response");
            registrationId = responseNaver.getOrDefault("id", "").toString();
            String name = responseNaver.getOrDefault("name", "").toString();
        } else if ("kakao".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
            registrationId = attributes.getOrDefault("id", "").toString();
        }
        logger.info("Start to find entering member");
        Member foundUser = memberService.getMemberByRegistrationId(registrationId);
        if (foundUser != null) {
            logger.info("{} + {} member found.", foundUser.getId(), foundUser.getMembername());
            sendJwtByCookie(foundUser, response);
        } else {
            logger.info("Creating Member.");
            Member newMember = Member.builder()
                    .registrationSource(RegistrationSource.valueOf(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId().toUpperCase()))
                    .registrationId(registrationId)
                    .build();
            sendJwtByCookie(newMember, response);
        }
        this.setAlwaysUseDefaultTargetUrl(true);
        this.setDefaultTargetUrl(frontendUrl);
        super.onAuthenticationSuccess(request, response, authentication);

    }

    public void sendJwtByCookie(Member member, HttpServletResponse response) {
        logger.info("Generating JWT.");
        SecretKey key = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));
        String jwt = Jwts.builder()
                .setClaims(Map.of("id", member.getId(),  "role", "ROLE_MEMBER"))
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
        logger.info("JWT token has been issued.");
    }
}

