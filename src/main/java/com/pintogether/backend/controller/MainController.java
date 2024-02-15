package com.pintogether.backend.controller;

import com.pintogether.backend.auth.JwtService;
import com.pintogether.backend.entity.Member;
import com.pintogether.backend.repository.MemberRepository;
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
    private MemberRepository memberRepository;

    /**
     *
     * jwt 로 닉네임 받아보는 테스트.
     *
     */
    @GetMapping("/test")
    public Map<String, String> test(@RequestHeader(value = "Authorization") String jwt) {
<<<<<<< HEAD
        String registrationPk = jwtService.getRegistrationKey(jwt);
        Map<String, String> map = new HashMap<>();
        Optional<User> user = userRepository.findByRegistrationKey(registrationPk);
        if (user.isPresent()) {
            map.put("nickname", user.get().getNickname());
=======
        String registrationId = jwtService.getRegistrationId(jwt);
        Map<String, String> map = new HashMap<>();
        Optional<Member> member = memberRepository.findByRegistrationId(registrationId);
        if (member.isPresent()) {
            map.put("nickname", member.get().getNickname());
>>>>>>> ddc274eded8d2db9c3079834de119c732b70390d
        } else {
            map.put("nickname", "AnonymousUser");
        }
        return map;
    }

}
