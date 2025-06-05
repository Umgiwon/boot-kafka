package com.one.bootkafka.global.enums.device;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DeviceStatus {
    RUNNING("가동중"),
    STOP("작동중지");

    private final String status;
}
