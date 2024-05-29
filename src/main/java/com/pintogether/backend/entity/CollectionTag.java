package com.pintogether.backend.entity;

import com.pintogether.backend.entity.enums.EntityStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@NoArgsConstructor
@Getter
@Where(clause = "entity_status='ACTIVE'")
@SQLDelete(sql = "UPDATE collection_tag SET entity_status='DELETE' WHERE id=?")
public class CollectionTag {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Collection collection;

    @NotNull
    private String tag;

    @Enumerated(EnumType.STRING)
    @NotNull
    private EntityStatus entityStatus;

    @Builder
    public CollectionTag(Collection collection, String tag, EntityStatus entityStatus) {
        this.collection = collection;
        this.tag = tag;
        this.entityStatus = entityStatus;
        this.collection.getCollectionTags().add(this);
    }
}
