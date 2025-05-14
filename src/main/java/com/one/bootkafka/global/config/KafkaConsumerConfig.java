package com.one.bootkafka.global.config;

import com.one.bootkafka.api.device.domain.dto.DeviceResponseDTO;
import com.one.bootkafka.global.constant.KafkaConst;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    /**
     * DeviceResponseDTO 형태의 메시지를 역직렬화할 JsonDeserializer 생성 후 설정
     * @return
     */
    @Bean
    public ConsumerFactory<String, DeviceResponseDTO> consumerFactory() {

        // TODO 역직렬화 처리
        JsonDeserializer<DeviceResponseDTO> deserializer = new JsonDeserializer<>(DeviceResponseDTO.class);

        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConst.KAFKA_BROKER_SERVER);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConst.KAFKA_GROUP_ID);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), deserializer);
    }

    /**
     * Kafka Consumer 설정 정의
     * @return
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, DeviceResponseDTO> kafkaListenerContainerFactory() {

        // @KafkaListener 어노테이션이 붙은 메서드에 주입되며, 메시지를 동시에 처리할 수 있는 메시지 리스터 컨테이너를 생성한다.
        ConcurrentKafkaListenerContainerFactory<String, DeviceResponseDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        return factory;
    }
}
