package com.pintogether.backend.entity;

import com.pintogether.backend.entity.components.Address;
import com.pintogether.backend.entity.enums.PlaceSource;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import javax.xml.transform.Source;

@Entity
@AllArgsConstructor
@Builder
@Getter
public class Place extends BaseUpdatedAtEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private PlaceSource placeSource;

    private String placeSourceId;

    private String phone;

    @NotNull
    private String name;

    @NotNull
    private String category;

    private String businessHour;

}
