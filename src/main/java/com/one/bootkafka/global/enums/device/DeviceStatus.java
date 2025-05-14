package com.one.bootkafka.global.enums.device;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DeviceStatus {
    RUN_Y("가동중"),
    RUN_N("작동중지")
    ;

    private final String status;
}
