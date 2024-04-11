package com.pintogether.backend.entity;

import com.pintogether.backend.entity.enums.NotificationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Enumerated(value = EnumType.STRING)
    @NotNull
    private NotificationType notificationType;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Member subject;

    private Long objectId;

    private String object;
}
