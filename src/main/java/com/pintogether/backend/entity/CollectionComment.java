package com.pintogether.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @ManyToOne(optional = false)
    private Member member;

    @NotNull
    private String contents;

    CollectionComment(Collection collection, Member member, String contents) {
        this.collection = collection;
        this.member = member;
        this.contents = contents;
        this.collection.getCollectionComments().add(this);
    }
}
