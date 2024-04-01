package com.pintogether.backend.websocket;

import lombok.Getter;

public enum NotificationType {
    FOLLOW("FOLLOW"),
    SCRAP_COLLECTION("SCRAP_COLLECTION"),
    COMMENT_ON_COLLECTION("COMMENT_ON_COLLECTION"),
    CREATE_COLLECTION("CREATE_COLLECTION"),
    LIKE_COLLECTION("LIKE_COLLECTION"),
    COMMENT_DELETED("COMMENT_DELETED");

    @Getter
    private final String string;

    NotificationType(String string) {
        this.string = string;
    }
}
