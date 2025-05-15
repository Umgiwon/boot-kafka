package com.one.bootkafka.global.config;

import com.one.bootkafka.api.device.domain.dto.DeviceDTO;
import com.one.bootkafka.global.constant.KafkaConst;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

@EnableKafka
@Slf4j
@Configuration
public class KafkaConsumerConfig {

    /**
     * DeviceDTO 형태의 메시지를 역직렬화할 JsonDeserializer 생성 후 설정
     * @return
     */
    @Bean
    public ConsumerFactory<String, DeviceDTO> consumerFactory() {

        JsonDeserializer<DeviceDTO> deserializer = new JsonDeserializer<>(DeviceDTO.class);

        deserializer.addTrustedPackages("*");

        Map<String, Object> config = Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConst.KAFKA_BROKER_SERVER,
                ConsumerConfig.GROUP_ID_CONFIG, KafkaConst.KAFKA_GROUP_ID,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer,
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest"
        );

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), deserializer);
    }

    /**
     * Kafka Consumer 설정 정의
     * @return
     */
    @Bean(name = KafkaConst.DEVICE_INFO_FACTORY)
    public ConcurrentKafkaListenerContainerFactory<String, DeviceDTO> kafkaListenerContainerFactory() {

        // @KafkaListener 어노테이션이 붙은 메서드에 주입되며, 메시지를 동시에 처리할 수 있는 메시지 리스터 컨테이너를 생성한다.
        ConcurrentKafkaListenerContainerFactory<String, DeviceDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        return factory;
    }
}
