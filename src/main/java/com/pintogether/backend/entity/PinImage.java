package com.pintogether.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class PinImage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Pin pin;

    @NotNull
    private String imagePath;

    @Builder
    public PinImage(Pin pin, String imagePath) {
        this.pin = pin;
        this.imagePath = imagePath;
        this.pin.getPinImages().add(this);
    }
}
