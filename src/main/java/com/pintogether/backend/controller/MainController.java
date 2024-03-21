package com.pintogether.backend.controller;

import com.pintogether.backend.auth.JwtService;
import com.pintogether.backend.customAnnotations.ThisMember;
import com.pintogether.backend.entity.Member;
import com.pintogether.backend.model.ApiResponse;
import com.pintogether.backend.repository.MemberRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping("/")
    public Long asdf(@ThisMember Member member) {
        return member.getId();
    }

    @GetMapping("/1")
    public ApiResponse sdf() {
        return ApiResponse.makeResponse(new String());
    }

    @Data
    static class dto {
        String a;

        dto(String a) {
            this.a = a;
        }
    }
}
