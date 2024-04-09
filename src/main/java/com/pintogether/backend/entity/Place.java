package com.pintogether.backend.entity;

import com.pintogether.backend.dto.CoordinateDTO;
import com.pintogether.backend.dto.ShowPlaceResponseDTO;
import com.pintogether.backend.entity.components.Address;
import com.pintogether.backend.entity.enums.PlaceSource;
import com.pintogether.backend.util.CoordinateConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
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

    public ShowPlaceResponseDTO toShowPlaceReponseDto(boolean starred, int pinCnt) {
        CoordinateDTO convertedCoordinate = CoordinateConverter.convert(this.getAddress().getLongitude(), this.getAddress().getLatitude());
        return ShowPlaceResponseDTO.builder()
                .id(this.getId())
                .name(this.getName())
                .roadNameAddress(this.getAddress().getRoadNameAddress())
                .placePinCnt(pinCnt)
                .category(this.getCategory())
                .starred(starred)
                .longitude(convertedCoordinate.getLongitude())
                .latitude(convertedCoordinate.getLatitude())
                .build();
    }
}
