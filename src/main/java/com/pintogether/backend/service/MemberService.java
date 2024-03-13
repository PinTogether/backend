package com.pintogether.backend.service;

import com.pintogether.backend.dto.UpdateMemberRequestDTO;
import com.pintogether.backend.entity.Member;
import com.pintogether.backend.exception.CustomException;
import com.pintogether.backend.model.StatusCode;
import com.pintogether.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    public Member findOneById(Long id) {
        return memberRepository.findOneById(id).orElse(null);
    }
    public void update(Long id, UpdateMemberRequestDTO updateMemberRequestDTO) {
        Member foundMember = this.findOneById(id);
        foundMember.updateMember(updateMemberRequestDTO);
    }

    public void delete(Long id) {
        Member foundMember = this.findOneById(id);
        memberRepository.delete(foundMember);
    }

    public List<Member> getFollowers(Long id) {
        return memberRepository.findAllFollowers(id);
    }

    public List<Member> getFollowings(Long id) {
        return memberRepository.findAllFollowings(id);
    }
}
