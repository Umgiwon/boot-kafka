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
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

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
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
        );

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), deserializer);
    }

    /**
     * Kafka 메시지 처리 중 오류 발생 시 사용할 에러 핸들러
     * 재시도 간격 및 최대 재시도 횟수를 설정
     * 
     * @return 설정된 에러 핸들러
     */
    @Bean
    public CommonErrorHandler errorHandler() {
        // 1초 간격으로 최대 3번 재시도
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(new FixedBackOff(1000L, 3));

        // 로그 레벨은 기본값(ERROR)을 사용

        // 에러 발생 시 로깅
        errorHandler.setCommitRecovered(true);

        log.info("[KafkaConsumer] 에러 핸들러 설정 완료");
        return errorHandler;
    }

    /**
     * Kafka Consumer 설정 정의
     * @return 설정된 Kafka 리스너 컨테이너 팩토리
     */
    @Bean(name = KafkaConst.DEVICE_INFO_FACTORY)
    public ConcurrentKafkaListenerContainerFactory<String, DeviceDTO> kafkaListenerContainerFactory() {

        // @KafkaListener 어노테이션이 붙은 메서드에 주입되며, 메시지를 동시에 처리할 수 있는 메시지 리스터 컨테이너를 생성한다.
        ConcurrentKafkaListenerContainerFactory<String, DeviceDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setCommonErrorHandler(errorHandler());

        return factory;
    }
}
