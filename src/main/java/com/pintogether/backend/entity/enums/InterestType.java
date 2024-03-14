package com.pintogether.backend.entity.enums;

import lombok.Getter;

public enum InterestType {
    LIKES("LIKES"),
    SCRAP("SCRAP");

    @Getter
    private final String string;

    InterestType(String string) {
        this.string = string;
    }
}
