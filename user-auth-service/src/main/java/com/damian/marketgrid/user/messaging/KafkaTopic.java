package com.damian.marketgrid.user.messaging;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum KafkaTopic {
    USER_CHANGED("user-changed");

    private final String topic;
}
