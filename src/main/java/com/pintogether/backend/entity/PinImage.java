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
@SQLDelete(sql = "UPDATE pin_image SET entity_status='DELETE' WHERE id=?")
public class PinImage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Pin pin;

    @NotNull
    private String imagePath;

    @Enumerated(EnumType.STRING)
    @NotNull
    private EntityStatus entityStatus;

    @Builder
    public PinImage(Pin pin, String imagePath, EntityStatus entityStatus) {
        this.pin = pin;
        this.imagePath = imagePath;
        this.entityStatus = entityStatus;
        this.pin.getPinImages().add(this);
    }
}
