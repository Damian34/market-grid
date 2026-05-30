package com.damian.marketgrid.messaging.producer

import com.damian.marketgrid.config.KafkaConfig
import com.damian.marketgrid.messaging.KafkaTopic
import com.damian.marketgrid.messaging.event.KafkaEvent
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory

@ApplicationScoped
class IngestProducer @Inject constructor(
    private val kafkaConfig: KafkaConfig,
    private val objectMapper: ObjectMapper
){
    private val logger = LoggerFactory.getLogger(IngestProducer::class.java)

    fun produce(payload: Any) {
        val kafkaTopic = KafkaTopic.of(payload)
        val kafkaEvent = KafkaEvent.of(payload)

        val payloadStr = objectMapper.writeValueAsString(kafkaEvent)
        val record = ProducerRecord(kafkaTopic.topic,kafkaEvent.eventId,payloadStr)
        kafkaConfig.kafkaProducer.send(record)

        logger.info("Emitted event [${kafkaTopic.name}] to topic [${kafkaTopic.topic}] for eventId: ${kafkaEvent.eventId}")
    }
}
