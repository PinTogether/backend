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
    @Column(nullable = false)
    private String registrationSource;

    private String roadNameAddress;

    private String roadNumberAddress;

    private String zipcode;

    @Column(nullable = false)
    private BigDecimal x;

    @Column(nullable = false)
    private BigDecimal y;
}
