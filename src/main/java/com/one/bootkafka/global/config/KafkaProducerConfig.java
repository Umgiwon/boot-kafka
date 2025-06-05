package com.one.bootkafka.global.config;

import com.one.bootkafka.global.constant.KafkaConst;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka Producer 설정 클래스
 * 메시지 생산자의 설정을 정의
 */
@Slf4j
@Configuration
public class KafkaProducerConfig {

    /**
     * Producer 인스턴스를 생성하는 방식 정의 후 리턴
     * 안정성과 성능을 위한 설정 포함
     * 
     * @return 설정된 ProducerFactory 인스턴스
     */
    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> config = new HashMap<>();

        // 기본 설정
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConst.KAFKA_BROKER_SERVER);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        // 안정성 설정
        config.put(ProducerConfig.ACKS_CONFIG, "all"); // 모든 복제본이 메시지를 받았는지 확인
        config.put(ProducerConfig.RETRIES_CONFIG, 3); // 실패 시 재시도 횟수
        config.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 1000); // 재시도 간격 (1초)

        // 성능 설정
        config.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384); // 배치 크기 (16KB)
        config.put(ProducerConfig.LINGER_MS_CONFIG, 10); // 배치 전송 전 대기 시간 (10ms)
        config.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432); // 버퍼 메모리 크기 (32MB)
        config.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy"); // 압축 타입

        log.info("[KafkaProducer] 프로듀서 설정 완료");
        return new DefaultKafkaProducerFactory<>(config);
    }

    /**
     * 직렬화된 메시지를 Kafka 브로커에 전송하는 역할
     * 
     * @return 설정된 KafkaTemplate 인스턴스
     */
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
