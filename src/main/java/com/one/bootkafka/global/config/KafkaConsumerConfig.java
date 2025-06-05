package com.one.bootkafka.global.config;

import com.one.bootkafka.api.device.domain.dto.DeviceDTO;
import com.one.bootkafka.api.device.domain.dto.ParsedDeviceDataDTO;
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
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.ExponentialBackOff;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka Consumer 설정 클래스
 * 메시지 소비자의 설정을 정의
 */
@EnableKafka
@Slf4j
@Configuration
public class KafkaConsumerConfig {

    /**
     * DeviceDTO 형태의 메시지를 역직렬화할 ConsumerFactory 생성 후 설정
     * 안정성과 성능을 위한 설정 포함
     * 
     * @return 설정된 ConsumerFactory 인스턴스
     */
    @Bean
    public ConsumerFactory<String, DeviceDTO> consumerFactory() {
        // 역직렬화 오류 처리를 위한 JsonDeserializer 설정
        JsonDeserializer<DeviceDTO> jsonDeserializer = new JsonDeserializer<>(DeviceDTO.class);
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.addTrustedPackages("com.one.bootkafka.api.device.domain.dto");
        jsonDeserializer.setUseTypeMapperForKey(true);

        // 역직렬화 오류 처리를 위한 ErrorHandlingDeserializer 래핑
        ErrorHandlingDeserializer<DeviceDTO> errorHandlingDeserializer = 
            new ErrorHandlingDeserializer<>(jsonDeserializer);

        Map<String, Object> config = new HashMap<>();

        // 기본 설정
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConst.KAFKA_BROKER_SERVER);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConst.KAFKA_GROUP_ID);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        config.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        config.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // 성능 및 안정성 설정
        config.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500); // 한 번에 가져올 최대 레코드 수
        config.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 300000); // 최대 폴링 간격 (5분)
        config.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 1024); // 최소 데이터 크기 (1KB)
        config.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 500); // 최대 대기 시간 (500ms)
        config.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 3000); // 하트비트 간격 (3초)
        config.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 30000); // 세션 타임아웃 (30초)

        log.info("[KafkaConsumer] 컨슈머 설정 완료");
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), errorHandlingDeserializer);
    }

    /**
     * ParsedDeviceDataDTO 형태의 메시지를 역직렬화할 ConsumerFactory 생성 후 설정
     * 
     * @return 설정된 ConsumerFactory 인스턴스
     */
    @Bean
    public ConsumerFactory<String, ParsedDeviceDataDTO> parsedDataConsumerFactory() {
        // 역직렬화 오류 처리를 위한 JsonDeserializer 설정
        ErrorHandlingDeserializer<ParsedDeviceDataDTO> errorHandlingDeserializer = getParsedDeviceDataDTOErrorHandlingDeserializer();

        Map<String, Object> config = new HashMap<>();

        // 기본 설정 (consumerFactory와 동일)
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConst.KAFKA_BROKER_SERVER);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConst.KAFKA_GROUP_ID);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        config.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        config.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // 성능 및 안정성 설정 (consumerFactory와 동일)
        config.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);
        config.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 300000);
        config.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 1024);
        config.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 500);
        config.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 3000);
        config.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 30000);

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), errorHandlingDeserializer);
    }

    private static ErrorHandlingDeserializer<ParsedDeviceDataDTO> getParsedDeviceDataDTOErrorHandlingDeserializer() {
        JsonDeserializer<ParsedDeviceDataDTO> jsonDeserializer = new JsonDeserializer<>(ParsedDeviceDataDTO.class);
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.addTrustedPackages("com.one.bootkafka.api.device.domain.dto");
        jsonDeserializer.setUseTypeMapperForKey(true);

        // 역직렬화 오류 처리를 위한 ErrorHandlingDeserializer 래핑
        ErrorHandlingDeserializer<ParsedDeviceDataDTO> errorHandlingDeserializer =
            new ErrorHandlingDeserializer<>(jsonDeserializer);
        return errorHandlingDeserializer;
    }

    /**
     * Kafka 메시지 처리 중 오류 발생 시 사용할 에러 핸들러
     * 지수 백오프를 사용하여 재시도 간격을 점진적으로 증가시킴
     * 
     * @return 설정된 에러 핸들러
     */
    @Bean
    public CommonErrorHandler errorHandler() {
        // 지수 백오프 설정: 초기 간격 1초, 최대 간격 10초, 승수 1.5, 최대 5번 재시도
        ExponentialBackOff backOff = new ExponentialBackOff(1000L, 1.5);
        backOff.setMaxElapsedTime(10000L);
        backOff.setMaxInterval(10000L);

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(backOff);

        // 특정 예외는 재시도하지 않도록 설정
        errorHandler.addNotRetryableExceptions(IllegalArgumentException.class);

        // 에러 발생 시 로깅
        errorHandler.setCommitRecovered(true);

        log.info("[KafkaConsumer] 에러 핸들러 설정 완료");
        return errorHandler;
    }

    /**
     * DeviceDTO를 위한 Kafka Consumer 설정 정의
     * 
     * @return 설정된 Kafka 리스너 컨테이너 팩토리
     */
    @Bean(name = KafkaConst.DEVICE_INFO_FACTORY)
    public ConcurrentKafkaListenerContainerFactory<String, DeviceDTO> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, DeviceDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setCommonErrorHandler(errorHandler());

        // 동시성 설정
        factory.setConcurrency(3); // 3개의 스레드로 병렬 처리

        // 배치 처리 설정
        factory.setBatchListener(false); // 단일 메시지 처리 모드

        // 커밋 설정
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD); // 레코드 단위 커밋

        log.info("[KafkaConsumer] 리스너 컨테이너 팩토리 설정 완료");
        return factory;
    }

    /**
     * ParsedDeviceDataDTO를 위한 Kafka Consumer 설정 정의
     * 
     * @return 설정된 Kafka 리스너 컨테이너 팩토리
     */
    @Bean(name = KafkaConst.PARSED_DEVICE_INFO_FACTORY)
    public ConcurrentKafkaListenerContainerFactory<String, ParsedDeviceDataDTO> parsedDataListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ParsedDeviceDataDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(parsedDataConsumerFactory());
        factory.setCommonErrorHandler(errorHandler());

        // 동시성 설정
        factory.setConcurrency(3); // 3개의 스레드로 병렬 처리

        // 배치 처리 설정
        factory.setBatchListener(false); // 단일 메시지 처리 모드

        // 커밋 설정
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD); // 레코드 단위 커밋

        return factory;
    }
}
