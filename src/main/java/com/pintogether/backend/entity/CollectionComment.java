package com.pintogether.backend.entity;

import com.pintogether.backend.entity.enums.EntityStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Where(clause = "entity_status='ACTIVE'")
@SQLDelete(sql = "UPDATE collection_comment SET entity_status='DELETE' WHERE id=?")
public class CollectionComment extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Collection collection;

    @ManyToOne(optional = false)
    private Member member;

    @NotNull
    private String contents;

    @Enumerated(EnumType.STRING)
    private EntityStatus entityStatus;

    CollectionComment(Collection collection, Member member, String contents) {
        this.collection = collection;
        this.member = member;
        this.contents = contents;
        this.collection.getCollectionComments().add(this);
    }
}
