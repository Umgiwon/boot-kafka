package com.one.bootkafka.api.device.controller;

import com.one.bootkafka.api.device.domain.dto.DeviceResponseDTO;
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
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Device API", description = "기기 API")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/device")
public class DeviceController {

    // WebSocket 메시지를 보내기 위한 템플릿
    private final SimpMessagingTemplate messagingTemplate;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "409", description = "데이터 중복", content = @Content(schema = @Schema(implementation = ExceptionMsg.class))),
            @ApiResponse(responseCode = "500", description = "서버내부 오류발생", content = @Content(schema = @Schema(implementation = ExceptionMsg.class)))
    })
    @Operation(summary = "소켓 테스트", description = "소켓 테스트 API")
    @MessageMapping("/test")
    public BaseResponse<DeviceResponseDTO> socketTest(@RequestBody DeviceResponseDTO deviceResponseDTO) {

        messagingTemplate.convertAndSend("/sub/severance-data", deviceResponseDTO);

        return BaseResponseFactory.success(deviceResponseDTO);
    }
}
