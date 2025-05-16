package com.one.bootkafka;

import com.one.bootkafka.global.constant.KafkaConst;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootTest
class BootKafkaApplicationTests {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Kafka Producer Test
     */
    @Test
    public void testKafkaProducer() {
        kafkaTemplate.send(KafkaConst.KAFKA_TOPIC, "test message");
    }
}
