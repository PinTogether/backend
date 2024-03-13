package com.pintogether.backend.service;

import com.pintogether.backend.entity.Following;
import com.pintogether.backend.entity.Member;
import com.pintogether.backend.exception.CustomException;
import com.pintogether.backend.model.ApiResponse;
import com.pintogether.backend.model.StatusCode;
import com.pintogether.backend.repository.FollowingRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowingService {
    private final FollowingRepository followingRepository;
    private final MemberService memberService;

    // member를  따르는 사람들
    public int getFollowerCnt(Long memberId) {
        return followingRepository.countByFolloweeId(memberId);
    }

    // member가 따르는 사람들
    public int getFolloweeCnt(Long memberId) {
        return followingRepository.countByFollowerId(memberId);
    }

    public boolean checkIfFollow(Long memberId, Long otherMemberId) {
        return followingRepository.existsByFollowerIdAndFolloweeId(memberId, otherMemberId);
    }

    public void follow(Long followerId, Long followeeId) {
        Following foundFollowing = followingRepository.findByFollowerIdAndFolloweeId(followerId, followeeId).orElse(null);
        if (foundFollowing != null) {
            throw new CustomException(StatusCode.BAD_REQUEST, "이미 팔로우 되어 있습니다.");
        }
        Following following = Following.builder()
                .follower(memberService.findOneById(followerId))
                .followee(memberService.findOneById(followeeId))
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
