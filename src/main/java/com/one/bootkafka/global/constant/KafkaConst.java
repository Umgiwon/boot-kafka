package com.one.bootkafka.global.constant;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE) // 인스턴스화 방지를 위한 private 생성자

public class KafkaConst {

    public static final String KAFKA_GROUP_ID = "comp";
    public static final String KAFKA_TOPIC = "modbus-data";
    public static final String KAFKA_BROKER_SERVER = "192.168.219.51:9092";

    public static final String DEVICE_INFO_FACTORY = "kafkaListenerContainerFactory";


    public static final String KAFKA_PARSED_TOPIC = "parsed-device-data";
}
