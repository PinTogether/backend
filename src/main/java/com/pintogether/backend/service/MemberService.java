package com.pintogether.backend.service;

import com.pintogether.backend.dto.UpdateMemberRequestDTO;
import com.pintogether.backend.entity.Member;
import com.pintogether.backend.entity.enums.InterestType;
import com.pintogether.backend.entity.enums.RegistrationSource;
import com.pintogether.backend.entity.enums.RoleType;
import com.pintogether.backend.exception.CustomException;
import com.pintogether.backend.model.StatusCode;
import com.pintogether.backend.repository.CollectionRepository;
import com.pintogether.backend.repository.FollowingRepository;
import com.pintogether.backend.repository.InterestingCollectionRepository;
import com.pintogether.backend.repository.MemberRepository;
import com.pintogether.backend.util.RandomNicknameGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final CollectionRepository collectionRepository;
    private final InterestingCollectionRepository interestingCollectionRepository;
    private final FollowingRepository followingRepository;
    private final AmazonS3Service amazonS3Service;
    public Member getMember(Long id) {
        return memberRepository.findOneById(id).orElse(null);
    }

    public void update(Member member, UpdateMemberRequestDTO updateMemberRequestDTO) {
        if (!member.getMembername().equals(updateMemberRequestDTO.getMembername()) &&
                memberRepository.existsOneByMembername(updateMemberRequestDTO.getMembername())) {
            throw new CustomException(StatusCode.BAD_REQUEST, "중복된 멤버이름 입니다.");
        }
        Set<String> defaultImage = new HashSet<>(Arrays.asList(
                "https://pintogether-img.s3.ap-northeast-2.amazonaws.com/default/profile1.png",
                "https://pintogether-img.s3.ap-northeast-2.amazonaws.com/default/profile2.png",
                "https://pintogether-img.s3.ap-northeast-2.amazonaws.com/default/profile3.png"
        ));
        if (!defaultImage.contains(member.getAvatar()) && !member.getAvatar().equals(updateMemberRequestDTO.getAvatar())) {
            amazonS3Service.deleteS3Image(member.getAvatar());
        }
        member.updateMember(updateMemberRequestDTO);
        memberRepository.save(member);
    }

    public void delete(Long id) {
        Member foundMember = this.getMember(id);
        memberRepository.delete(foundMember);
    }

    public int getScrappedCollectionCnt(Long memberId) {
        return interestingCollectionRepository.countByMemberIdAndInterestType(memberId, InterestType.SCRAP);
    }

    public int getLikeCollectionCnt(Long memberId) {
        return interestingCollectionRepository.countByMemberIdAndInterestType(memberId, InterestType.LIKES);
    }

    public int getFollowerCnt(Long memberId) {
        return followingRepository.countByFolloweeId(memberId);
    }

    public int getFolloweeCnt(Long memberId) {
        return followingRepository.countByFollowerId(memberId);
    }

    public List<Member> getFollowers(Long id) {
        return memberRepository.findAllFollowers(id);
    }

    public List<Member> getFollowings(Long id) {
        return memberRepository.findAllFollowings(id);
    }

    public int getCollectionCnt(Long memberId) {
        return collectionRepository.countByMemberId(memberId);
    }

    public AmazonS3Service.AmazonS3Response getPresignedUrl(String contentType, String domainType, Long id) {
        return amazonS3Service.getneratePresignedUrlAndImageUrl(contentType, domainType, id);
    }

    public Member getMemberByRegistrationId(String registrationId) {
        return memberRepository.findByRegistrationId(registrationId).orElse(null);
    }

    public boolean checkIfDuplicatedMembername(String membername) {
        return memberRepository.existsOneByMembername(membername);
    }

    public Member getMemberByMembername(String membername) {
        return memberRepository.findOneByMembername(membername).orElse(null);
    }

    public void increaeAlertCnt(Member member) {
        member.increaseAlertCnt();
        memberRepository.save(member);
    }

    public void clearAlertCnt(Member member) {
        member.clearAlertCnt();
        memberRepository.save(member);
    }
}
