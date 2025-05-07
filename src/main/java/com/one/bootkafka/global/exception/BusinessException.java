package com.one.bootkafka.global.exception;

import com.one.bootkafka.global.enums.common.ApiReturnCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BusinessException extends RuntimeException {

    private ApiReturnCode apiReturnCode;
}
