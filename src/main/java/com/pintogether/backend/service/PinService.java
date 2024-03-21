package com.pintogether.backend.service;

import com.pintogether.backend.dto.CreatePinRequestDTO;
import com.pintogether.backend.dto.CreateSelectedPinsRequestDTO;
import com.pintogether.backend.dto.DeleteSelectedPinsRequestDTO;
import com.pintogether.backend.dto.UpdatePinReqeustDTO;
import com.pintogether.backend.entity.*;
import com.pintogether.backend.exception.CustomException;
import com.pintogether.backend.model.CustomStatusMessage;
import com.pintogether.backend.model.StatusCode;
import com.pintogether.backend.repository.CollectionRepository;
import com.pintogether.backend.repository.PinRepository;
import com.pintogether.backend.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PinService {

    private final CollectionRepository collectionRepository;
    private final PinRepository pinRepository;
    private final PlaceRepository placeRepository;
    private final AmazonS3Service amazonS3Service;

    public void createPin(Member member, CreatePinRequestDTO createPinRequestDTO) {
        Place place = placeRepository.findById(createPinRequestDTO.getPlaceId()).orElseThrow(
                ()-> new CustomException(StatusCode.NOT_FOUND, CustomStatusMessage.PLACE_NOT_FOUND.getMessage())
        );
        Collection collection = collectionRepository.findOneById(createPinRequestDTO.getCollectionId()).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, CustomStatusMessage.COLLECTION_NOT_FOUND.getMessage())
        );
        if (!member.getId().equals(collection.getMember().getId())) {
            throw new CustomException(StatusCode.FORBIDDEN, "해당 컬렉션에 핀을 생성할 권한이 없습니다.");
        }
        Pin pin = Pin.builder()
                .place(place)
                .collection(collection)
                .review(createPinRequestDTO.getReview())
                .build();
        if (createPinRequestDTO.getTags() != null) {
            for (String x : createPinRequestDTO.getTags()) {
                pin.getPinTags().add(
                        PinTag.builder()
                                .pin(pin)
                                .tag(x)
                                .build());
            }
        }
        if (createPinRequestDTO.getImagePaths() != null) {
            for (String x : createPinRequestDTO.getImagePaths()) {
                pin.getPinImages().add(
                        PinImage.builder()
                                .pin(pin)
                                .imagePath(x)
                                .build()
                );
            }
        }
        pinRepository.save(pin);
    }

    public void createSelectedPins(Member member, CreateSelectedPinsRequestDTO dto) {
        for (Long placeId : dto.getPlaceId()) {
            createPin(member, CreatePinRequestDTO.builder()
                    .collectionId(dto.getCollectionId())
                    .placeId(placeId)
                    .review("")
                    .build());
        }
    }

    public void updatePin(Member member, Long id, UpdatePinReqeustDTO updatePinReqeustDTO) {
        Pin pin = pinRepository.findById(id).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, "해당 핀을 찾을 수 없습니다.")
        );
        if (pin.getCollection().getMember().getId() != member.getId()) {
            throw new CustomException(StatusCode.FORBIDDEN, "해당 핀을 수정할 권한이 없습니다.");
        }
        pin.clearPinTagsAndImages();

        for (String x : updatePinReqeustDTO.getTags()) {
            pin.getPinTags().add(PinTag.builder()
                            .tag(x)
                            .pin(pin)
                            .build());
        }
        for (String x : updatePinReqeustDTO.getImagePaths()) {
            pin.getPinImages().add(PinImage.builder()
                            .imagePath(x)
                            .pin(pin)
                            .build());
        }
        pinRepository.save(pin);
    }

    public Pin getPin(Long pinId) {
        return pinRepository.findById(pinId).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, "해당 핀을 찾을 수 없습니다.")
        );
    }

    public void deletePin(Member member, Long id) {
        Pin pin = this.getPin(id);
        if (!member.getId().equals(pin.getCollection().getMember().getId())) {
            throw new CustomException(StatusCode.FORBIDDEN, "해당 핀을 삭제할 권한이 없습니다.");
        }
        pinRepository.delete(pin);
    }

    public void deleteSelectedPins(Member member, DeleteSelectedPinsRequestDTO dto) {
        for (Long id : dto.getId()) {
            Pin pin = this.getPin(id);
            if (!member.getId().equals(pin.getCollection().getMember().getId())) {
                throw new CustomException(StatusCode.FORBIDDEN, "해당 핀을 삭제할 권한이 없습니다.");
            }
            pinRepository.delete(pin);
        }
    }

    public AmazonS3Service.AmazonS3Response getPresignedUrlForPinImage(Long memberId, String contentType, String domainType, Long id) {
        Pin pin = getPin(id);
        if (!memberId.equals(pin.getCollection().getMember().getId())) {
            throw new CustomException(StatusCode.FORBIDDEN, "핀 이미지 생성에 필요한 presigned url을 요청할 권한이 없습니다.");
        }
        return amazonS3Service.getneratePresignedUrlAndImageUrl(contentType, domainType, id);
    }

}
