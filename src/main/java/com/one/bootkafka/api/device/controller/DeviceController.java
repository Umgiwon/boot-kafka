package com.one.bootkafka.api.device.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Device API", description = "기기 API")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/device")
public class DeviceController {


}
