package com.damian.marketgrid.messaging.event

import java.time.Instant
import java.util.UUID

data class KafkaEvent<T>(
    val eventId: String,
    val occurredAt: Instant,
    val payload: T
) {
    companion object {
        fun <T> of(payload: T): KafkaEvent<T> =
            KafkaEvent(UUID.randomUUID().toString(), Instant.now(), payload)
    }
}
