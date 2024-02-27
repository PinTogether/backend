package com.pintogether.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class PinTag {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Pin pin;

    @NotNull
    private String tag;

    @Builder
    public PinTag(Pin pin, String tag) {
        this.pin = pin;
        this.tag = tag;
        this.pin.getPinTags().add(this);
    }
}
