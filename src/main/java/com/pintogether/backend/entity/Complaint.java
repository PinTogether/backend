package com.pintogether.backend.entity;

import com.pintogether.backend.entity.enums.ComplaintCategory;
import com.pintogether.backend.entity.enums.PlatformType;
import com.pintogether.backend.entity.enums.Progress;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@AllArgsConstructor
@Getter
@RequiredArgsConstructor
public class Complaint extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "error: you missed platformType.")
    @Enumerated(value = EnumType.STRING)
    private PlatformType platformType;

    @ManyToOne
    @JoinColumn(name = "reporter_id")
    private Member reporter;

    @ManyToOne
    @JoinColumn(name = "target_member_id")
    private Member targetMember;

    @Enumerated(value = EnumType.STRING)
    @NotNull(message = "error: progress not found")
    private Progress progress;

    @Enumerated(value = EnumType.STRING)
    @NotNull(message = "category")
    private ComplaintCategory complaintCategory;

    @NotNull(message = "error: you missed reason")
    private String reason;

    @NotNull(message = "error: you missed targetId")
    private Long targetId;

    @Builder
    public Complaint(Member reporter, Member targetMember, PlatformType platformType,
                     ComplaintCategory complaintCategory, String reason, Long targetId) {
        this.reporter = reporter;
        this.targetMember = targetMember;
        this.progress = Progress.RECEIVED;
        this.platformType = platformType;
        this.complaintCategory = complaintCategory;
        this.reason = reason;
        this.targetId = targetId;
    }

}
