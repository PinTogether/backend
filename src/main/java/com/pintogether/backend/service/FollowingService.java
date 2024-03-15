package com.pintogether.backend.service;

import com.pintogether.backend.entity.Following;
import com.pintogether.backend.exception.CustomException;
import com.pintogether.backend.model.StatusCode;
import com.pintogether.backend.repository.FollowingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowingService {
    private final FollowingRepository followingRepository;
    private final MemberService memberService;

    public boolean checkIfFollow(Long memberId, Long otherMemberId) {
        return followingRepository.existsByFollowerIdAndFolloweeId(memberId, otherMemberId);
    }

    public void follow(Long followerId, Long followeeId) {
        Following foundFollowing = followingRepository.findByFollowerIdAndFolloweeId(followerId, followeeId).orElse(null);
        if (foundFollowing != null) {
            throw new CustomException(StatusCode.BAD_REQUEST, "이미 팔로우 되어 있습니다.");
        }
        Following following = Following.builder()
                .follower(memberService.getMember(followerId))
                .followee(memberService.getMember(followeeId))
                .build();
        followingRepository.save(following);
    }

    public void unfollow(Long followerId, Long followeeId) {
        Following foundFollowing = followingRepository.findByFollowerIdAndFolloweeId(followerId, followeeId).orElse(null);
        if (foundFollowing == null) {
            throw new CustomException(StatusCode.BAD_REQUEST, "팔로우하지 않은 멤버입니다.");
        }
        followingRepository.delete(foundFollowing);
    }
}
