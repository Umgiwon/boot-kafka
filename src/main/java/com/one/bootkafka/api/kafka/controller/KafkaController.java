package com.one.bootkafka.api.kafka.controller;

import com.one.bootkafka.api.device.domain.dto.DeviceDTO;
import com.one.bootkafka.api.kafka.service.KafkaProducerService;
import com.one.bootkafka.global.constant.KafkaConst;
import com.one.bootkafka.global.domain.dto.BaseResponse;
import com.one.bootkafka.global.domain.dto.BaseResponseFactory;
import com.one.bootkafka.global.exception.ExceptionMsg;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Device API", description = "기기 API")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/kafka")
public class KafkaController {

    private final KafkaProducerService kafkaProducerService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "500", description = "서버내부 오류발생", content = @Content(schema = @Schema(implementation = ExceptionMsg.class)))
    })
    @Operation(summary = "카프카 테스트", description = "카프카 테스트 API")
    @PostMapping("/sendTest")
    public BaseResponse<DeviceDTO> sendMessage(@RequestBody DeviceDTO deviceDTO) {
        return BaseResponseFactory.success(kafkaProducerService.kafkaProducer(deviceDTO));
    }
}
