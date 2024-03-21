package com.pintogether.backend.dto;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PinsResponseDTO {

    List<PinDTO> pins;

}
