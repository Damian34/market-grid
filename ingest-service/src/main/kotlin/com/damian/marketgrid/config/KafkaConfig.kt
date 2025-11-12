package com.damian.marketgrid.config

import com.damian.marketgrid.messaging.KafkaTopicProduce
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.AlterConfigOp
import org.apache.kafka.clients.admin.ConfigEntry
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.config.ConfigResource
import org.apache.kafka.common.serialization.StringSerializer
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.util.Properties
import java.util.concurrent.TimeUnit

@ApplicationScoped
class KafkaConfig {

    private lateinit var producer: KafkaProducer<String, String>

    val kafkaProducer: KafkaProducer<String, String> get() = producer

    @ConfigProperty(name = "kafka.bootstrap.servers", defaultValue="None")
    var bootstrapServers: String = ""

    @ConfigProperty(name = "kafka.topic.retention-ms", defaultValue="604800")
    var retentionMs: Long = 0L

    @ConfigProperty(name = "kafka.topic.segment-ms", defaultValue="604800")
    var segmentMs: Long = 0L

    @PostConstruct
    fun init() {
        producer = KafkaProducer(createProperties())
        KafkaTopicProduce.entries.forEach { topicEntry ->
            createOrUpdateTopic(topicEntry.topic)
        }
    }

    private fun createProperties(): Properties {
        return Properties().also {
            it[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
            it[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
            it[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
            it[ProducerConfig.ACKS_CONFIG] = "all"
            it[ProducerConfig.RETRIES_CONFIG] = 3
            it[ProducerConfig.LINGER_MS_CONFIG] = 5
        }
    }

    private fun createOrUpdateTopic(topic: String) {
        AdminClient.create(Properties().apply { put("bootstrap.servers", bootstrapServers) }).use { admin ->
            val existingTopics = admin.listTopics().names().get(10, TimeUnit.SECONDS)
            if (!existingTopics.contains(topic)) {
                createTopic(admin, topic)
            } else {
                updateTopicConfig(admin, topic)
            }
        }
    }

    private fun createTopic(admin: AdminClient, topic: String) {
        val newTopic = NewTopic(topic, 1, 1).configs(
            mapOf(
                "retention.ms" to retentionMs.toString(),
                "segment.ms" to segmentMs.toString()
            )
        )
        admin.createTopics(listOf(newTopic))
            .all().get(10, TimeUnit.SECONDS)
    }

    private fun updateTopicConfig(admin: AdminClient, topic: String) {
        val resource = ConfigResource(ConfigResource.Type.TOPIC, topic)
        val configEntries = listOf(
            AlterConfigOp(
                ConfigEntry("retention.ms", retentionMs.toString()),
                AlterConfigOp.OpType.SET
            ),
            AlterConfigOp(
                ConfigEntry("segment.ms", segmentMs.toString()),
                AlterConfigOp.OpType.SET
            )
        )
        admin.incrementalAlterConfigs(mapOf(resource to configEntries))
            .all().get(10, TimeUnit.SECONDS)
    }
}

