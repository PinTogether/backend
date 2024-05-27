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
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Where(clause = "entity_status='ACTIVE'")
@SQLDelete(sql = "UPDATE collection SET entity_status='DELETE' WHERE id=?")
public class Collection extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Member member;

    @NotNull
    private String title;

    @NotNull
    private String thumbnail;

    @NotNull
    private String details;

    @OneToMany(mappedBy = "collection", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<CollectionTag> collectionTags = new ArrayList<>();

    @OneToMany(mappedBy = "collection", cascade = CascadeType.REMOVE)
    private final List<CollectionComment> collectionComments = new ArrayList<>();

    @OneToMany(mappedBy = "collection", cascade = CascadeType.REMOVE)
    private final List<Pin> pins = new ArrayList<>();

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private EntityStatus entityStatus;

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void updateDetails(String details) {
        this.details = details;
    }

    public void clearCollectionTags() {
        collectionTags.clear();
    }
}
