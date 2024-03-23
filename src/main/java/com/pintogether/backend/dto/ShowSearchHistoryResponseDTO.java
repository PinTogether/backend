package com.pintogether.backend.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ShowSearchHistoryResponseDTO {

    private Long id;

    private String query;

}
