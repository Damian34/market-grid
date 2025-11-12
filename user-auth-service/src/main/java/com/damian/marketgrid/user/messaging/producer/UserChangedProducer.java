package com.damian.marketgrid.user.messaging.producer;

import com.damian.marketgrid.user.messaging.KafkaTopic;
import com.damian.marketgrid.user.messaging.event.KafkaEvent;
import com.damian.marketgrid.user.messaging.event.UserChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserChangedProducer {

    private final KafkaTemplate<String, KafkaEvent<UserChangedEvent>> kafkaTemplate;

    public void produce(UserChangedEvent event) {
        KafkaEvent<UserChangedEvent> kafkaEvent = new KafkaEvent<>(event);
        kafkaTemplate.send(KafkaTopic.USER_CHANGED.getTopic(), kafkaEvent.eventId(), kafkaEvent);

        log.info("Emitted event [USER_CHANGED] to topic [{}] for userId: {}, eventId: {}",
                KafkaTopic.USER_CHANGED.getTopic(),
                event.id(),
                kafkaEvent.eventId());
    }
}
