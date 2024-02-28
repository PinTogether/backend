package com.pintogether.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
@Entity
@NoArgsConstructor
@Getter
public class CollectionTag {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Collection collection;

    @NotNull
    private String tag;

    @Builder
    public CollectionTag(Collection collection, String tag) {
        this.collection = collection;
        this.tag = tag;
        this.collection.getCollectionTags().add(this);
    }
}
