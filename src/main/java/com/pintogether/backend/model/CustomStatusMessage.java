package com.pintogether.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public enum CustomStatusMessage {
    MEMBER_NOT_FOUND("존재하지 않는 멤버입니다."),
    COLLECTION_NOT_FOUND("존재하지 않는 컬렉션입니다."),
    PLACE_NOT_FOUND("존재하지 않는 장소입니다."),
    SIZE_EXCEEDED("저장 가능한 갯수를 초과하였습니다.");

    @Getter
    private final String message;

    CustomStatusMessage(String message) {
        this.message = message;
    }

}
