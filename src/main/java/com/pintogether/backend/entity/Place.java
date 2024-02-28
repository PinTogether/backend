package com.pintogether.backend.entity;

import com.pintogether.backend.entity.components.Address;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Place {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Address address;

    @NotNull
    private String localManageCode;

    private String phone;

    @NotNull
    private String name;

    @NotNull
    private String category;

    private String businessHour;
}
