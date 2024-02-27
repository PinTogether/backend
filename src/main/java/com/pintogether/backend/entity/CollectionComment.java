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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Collection collection;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE) // 이거 중복으로 필요한가?
    private Member member;

    @NotNull
    private String contents;

    public void updateContents(String contents) {
        this.contents = contents;
    }
}
