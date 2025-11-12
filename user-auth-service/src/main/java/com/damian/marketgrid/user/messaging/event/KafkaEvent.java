package com.damian.marketgrid.user.messaging.event;

import java.time.Instant;
import java.util.UUID;

public record KafkaEvent<T>(
        String eventId,
        Instant occurredAt,
        T payload
){
    public KafkaEvent(T payload) {
        this(UUID.randomUUID().toString(), Instant.now(), payload);
    }
}
