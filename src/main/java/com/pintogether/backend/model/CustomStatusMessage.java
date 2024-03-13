package com.pintogether.backend.model;

import lombok.Getter;

public enum CustomStatusMessage {
    MEMBER_NOT_FOUND("존재하지 않는 멤버입니다."),
    COLLECTION_NOT_FOUND("존재하지 않는 컬렉션입니다.");

    @Getter
    private final String message;

    CustomStatusMessage(String message) {
        this.message = message;
    }
}
