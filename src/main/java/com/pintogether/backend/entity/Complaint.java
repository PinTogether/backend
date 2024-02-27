package com.pintogether.backend.entity;

import com.pintogether.backend.entity.enums.ComplaintCategory;
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
@Builder
@Getter
public class Complaint extends BaseEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Member reporter;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Member picked;

    @Enumerated(value = EnumType.STRING)
    @NotNull
    private ComplaintCategory complaintCategory;

    @NotNull
    private String reason;

    @ManyToOne
    private Pin pin;

    @ManyToOne
    private Collection collection;

    @ManyToOne
    private  CollectionComment collectionComment;

}
