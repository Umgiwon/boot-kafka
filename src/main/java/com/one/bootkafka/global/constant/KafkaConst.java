package com.one.bootkafka.global.constant;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE) // 인스턴스화 방지를 위한 private 생성자

public class KafkaConst {

    public static final String KAFKA_GROUP_ID = "comp";
    public static final String KAFKA_TOPIC = "severance-data";
    public static final String KAFKA_BROKER_SERVER = "localhost:9092";

    public static final String DEVICE_INFO_FACTORY = "kafkaListenerContainerFactory";
}
