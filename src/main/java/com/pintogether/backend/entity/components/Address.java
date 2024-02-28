package com.pintogether.backend.entity.components;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    private String roadNameAddress;

    private String numberAddress;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

}
