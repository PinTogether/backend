package com.pintogether.backend.dto;

import com.pintogether.backend.entity.PinImage;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ShowPinImagesResponseDTO {

    private final String[] imagePaths;

    @Builder
    public ShowPinImagesResponseDTO(List<PinImage> pinImages) {
        imagePaths = new String[pinImages.size()];
        for (int i = 0; i < pinImages.size(); i++) {
            this.imagePaths[i] = pinImages.get(i).getImagePath();
        }
    }

}
