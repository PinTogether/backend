package com.pintogether.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
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

    public void updateCollection(String title, String thumbnail, String details) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.details = details;
    }
}
