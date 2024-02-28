package com.pintogether.backend.entity;

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
public class CollectionComment extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Collection collection;

    @ManyToOne()
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Member member;

    @NotNull
    private String contents;
}
