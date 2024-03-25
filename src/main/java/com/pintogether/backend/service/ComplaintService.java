package com.pintogether.backend.service;

import com.pintogether.backend.dto.CreateComplaintRequestDTO;
import com.pintogether.backend.dto.ShowComplaintResponseDTO;
import com.pintogether.backend.entity.Complaint;
import com.pintogether.backend.entity.Member;
import com.pintogether.backend.repository.ComplaintRepository;
import com.pintogether.backend.util.DateConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final MemberService memberService;
    @Transactional
    public void createComplaint(Member reporter, Member targetMember, CreateComplaintRequestDTO dto) {
        complaintRepository.save(Complaint.builder()
                .reporter(reporter)
                .targetMember(targetMember)
                .platformType(dto.getPlatformType())
                .complaintCategory(dto.getComplaintCategory())
                .reason(dto.getReason())
                .targetId(dto.getTargetId())
                .build()
        );
    }

    public List<ShowComplaintResponseDTO> getComplaintList(Member member, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Complaint> complaintPage = complaintRepository.findByReporterId(pageable, member.getId());
        return complaintPage.stream()
                .map(c -> ShowComplaintResponseDTO.builder()
                        .id(c.getId())
                        .platformType(c.getPlatformType())
                        .reporterId(member.getId())
                        .reporterNickname(member.getNickname())
                        .targetMemberId(c.getTargetMember().getId())
                        .targetMemberNickname(c.getTargetMember().getNickname())
                        .createdAt(DateConverter.convert(c.getCreatedAt()))
                        .progress(c.getProgress())
                        .complaintCategory(c.getComplaintCategory())
                        .reason(c.getReason())
                        .targetId(c.getTargetId())
                        .build())
                .toList();

    }



}
