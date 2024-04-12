package com.pintogether.backend.entity;

import com.pintogether.backend.dto.ShowPinResponseDTO;
import com.pintogether.backend.util.DateConverter;
import jakarta.persistence.*;
import lombok.*;

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
        PinImage.builder()
                .imagePath(imagePath)
                .pin(this)
                .build();
    }

    public void updateTag(String tag) {
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

    public ShowPinResponseDTO toShowPinResponseDTO() {
        String[] images = new String[this.pinImages.size()];
        String[] tags = new String[this.pinTags.size()];
        for (int i = 0; i < this.pinImages.size(); i++) {
            images[i] = this.pinImages.get(i).getImagePath();
        }
        for (int i = 0; i < this.pinTags.size(); i++) {
            tags[i] = this.pinTags.get(i).getTag();
        }
        return ShowPinResponseDTO.builder()
                .id(this.id)
                .placeId(this.getPlace().getId())
                .placeName(this.getPlace().getName())
                .collectionId(this.getCollection().getId())
                .collectionTitle(this.getCollection().getTitle())
                .writerMembername(this.getCollection().getMember().getMembername())
                .writerId(this.getCollection().getMember().getId())
                .avatarImage(this.getCollection().getMember().getAvatar())
                .review(this.review)
                .createdAt(DateConverter.convert(this.getCreatedAt()))
                .imagePaths(images)
                .tags(tags)
                .build();
    }

    public void clearPinTagsAndImages() {
        pinTags.clear();
        pinImages.clear();
    }

}