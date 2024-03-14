package com.pintogether.backend.entity;

import com.pintogether.backend.entity.enums.RegistrationSource;
import com.pintogether.backend.entity.enums.RoleType;
import com.pintogether.backend.exception.CustomException;
import com.pintogether.backend.model.CustomStatusMessage;
import com.pintogether.backend.model.StatusCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Pin extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    private Place place;

    @ManyToOne(optional = false)
    private Collection collection;

    @Column(length = 1000)
    private String review;

    @OneToMany(mappedBy = "pin", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<PinTag> pinTags = new ArrayList<>();

    @OneToMany(mappedBy = "pin", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<PinImage> pinImages = new ArrayList<>();
    public void updateReview(String review) {
        this.review = review;
    }

    public void updateImage(String imagePath) {
        if (this.pinImages.size() > 5) {
            throw new CustomException(StatusCode.BAD_REQUEST, CustomStatusMessage.SIZE_EXCEEDED.getMessage());
        }
        PinImage.builder()
                .imagePath(imagePath)
                .pin(this)
                .build();
    }

    public void updateTag(String tag) {
        if (this.pinTags.size() > 5) {
            throw new CustomException(StatusCode.BAD_REQUEST, CustomStatusMessage.SIZE_EXCEEDED.getMessage());
        }
        PinTag.builder()
                .pin(this)
                .tag(tag)
                .build();
    }

    public void changeCollection(Collection collection) {
        this.collection = collection;
    }

    public void deletePlace() { this.place = null; }

    @Builder
    public Pin(Place place, Collection collection, String review, List<PinTag> pinTags, List<PinImage> pinImages) {
        this.place = place;
        this.collection = collection;
        this.review = review;
        this.pinTags.addAll(pinTags);
        this.pinImages.addAll(pinImages);
    }

}