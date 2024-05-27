package com.pintogether.backend.service;

import com.pintogether.backend.dto.CreateComplaintRequestDTO;
import com.pintogether.backend.dto.ShowComplaintResponseDTO;
import com.pintogether.backend.entity.Collection;
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

import java.util.ArrayList;
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
        Page<Complaint> complaints = complaintRepository.findByReporterId(pageable, member.getId());

        return complaints.stream().map(
                complaint -> ShowComplaintResponseDTO.builder()
                        .id(complaint.getId())
                        .platformType(complaint.getPlatformType())
                        .reporterId(member.getId())
                        .reporterMembername(member.getMembername())
                        .targetMemberId(complaint.getTargetMember() != null ? complaint.getTargetMember().getId() : -1)
                        .targetMembername(complaint.getTargetMember() != null ? complaint.getTargetMember().getMembername() : "탈퇴한 회원")
                        .createdAt(DateConverter.convert(complaint.getCreatedAt()))
                        .progress(complaint.getProgress())
                        .complaintCategory(complaint.getComplaintCategory())
                        .reason(complaint.getReason())
                        .targetId(complaint.getTargetId() != null ? complaint.getTargetId() : -1)
                        .build()).toList();
    }
}
