package com.pintogether.backend.controller;

import com.pintogether.backend.auth.JwtService;
import com.pintogether.backend.domain.User;
import com.pintogether.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class MainController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    /**
     *
     * jwt 로 닉네임 받아보는 테스트.
     *
     */
    @GetMapping("/test")
    public Map<String, String> test(@RequestHeader(value = "Authorization") String jwt) {
        String registrationId = jwtService.getregistrationId(jwt);
        Map<String, String> map = new HashMap<>();
        Optional<User> user = userRepository.findByregistrationId(registrationId);
        if (user.isPresent()) {
            map.put("nickname", user.get().getNickname());
        } else {
            map.put("nickname", "AnonymousUser");
        }
        return map;
    }

}
