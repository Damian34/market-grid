package com.damian.marketgrid.steps

import com.damian.marketgrid.config.KafkaConfig
import com.damian.marketgrid.messaging.producer.IngestProducer
import com.fasterxml.jackson.databind.ObjectMapper

class IngestProducerMock: IngestProducer(KafkaConfig(), ObjectMapper()) {
private val events = mutableListOf<Any>()

    override fun produce(payload: Any) {
        events.add(payload)
    }

    fun hasEventOfType(type: Class<*>): Boolean = events.any { type.isInstance(it) }

    fun clear() = events.clear()
}