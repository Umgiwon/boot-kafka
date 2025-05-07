package com.one.bootkafka.global.constant;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE) // 인스턴스화 방지를 위한 private 생성자
public final class ResponseMessageConst {

    /* 공통 응답 */
    public static final String SELECT_SUCCESS = "정상적으로 조회되었습니다.";
    public static final String NO_CONTENT = "조회된 데이터가 없습니다.";
}
