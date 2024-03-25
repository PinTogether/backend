package com.pintogether.backend.dto;

import com.pintogether.backend.entity.Complaint;
import com.pintogether.backend.entity.Member;
import com.pintogether.backend.entity.enums.ComplaintCategory;
import com.pintogether.backend.entity.enums.PlatformType;
import lombok.Getter;

@Getter
public class CreateComplaintRequestDTO {

    private PlatformType platformType;

    private ComplaintCategory complaintCategory;

    private String reason;

    private Long targetId;

}
