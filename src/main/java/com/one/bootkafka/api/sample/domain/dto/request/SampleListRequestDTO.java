package com.one.bootkafka.api.sample.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link com.one.bootkafka.api.sample.domain.entity.Sample}
 */
@Schema(description = "샘플 목록 조회 요청 DTO")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true) // json 데이터를 java 객체로 역직렬화 할 때 매핑되지 않은 필드를 무시
public class SampleListRequestDTO {

    @Schema(description = "제목 입력값", example = "조회할 샘플의 제목")
    private String title;

    @Schema(description = "내용 입력값", example = "조회할 샘플의 내용")
    private String content;
}
