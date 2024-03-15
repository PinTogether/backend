package com.pintogether.backend.service;

import com.pintogether.backend.dto.UpdateMemberRequestDTO;
import com.pintogether.backend.entity.Member;
import com.pintogether.backend.entity.enums.InterestType;
import com.pintogether.backend.repository.CollectionRepository;
import com.pintogether.backend.repository.FollowingRepository;
import com.pintogether.backend.repository.InterestingCollectionRepository;
import com.pintogether.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public void update(Long id, UpdateMemberRequestDTO updateMemberRequestDTO) {
        Member foundMember = this.getMember(id);
        foundMember.updateMember(updateMemberRequestDTO);
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
}
