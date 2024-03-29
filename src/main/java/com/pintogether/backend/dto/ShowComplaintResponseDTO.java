package com.pintogether.backend.dto;

import com.pintogether.backend.entity.enums.ComplaintCategory;
import com.pintogether.backend.entity.enums.PlatformType;
import com.pintogether.backend.entity.enums.Progress;
import lombok.*;

@Builder
@Getter
public class ShowComplaintResponseDTO {

    private Long id;

    private PlatformType platformType;

    private Long reporterId;

    private String reporterMembername;

    private Long targetMemberId;

    private String targetMembername;

    private String createdAt;

    private Progress progress;

    private ComplaintCategory complaintCategory;

    private String reason;

    private Long targetId;

}
