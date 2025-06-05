package com.one.bootkafka.api.device.service;

import com.one.bootkafka.api.device.domain.dto.DeviceDTO;
import com.one.bootkafka.api.device.domain.entity.Device;
import com.one.bootkafka.api.device.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * 트랜잭션 디바이스 작업을 위한 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DeviceServiceTx {

    private final DeviceRepository deviceRepository;

    /**
     * 디바이스 데이터를 데이터베이스에 저장
     * 
     * @param deviceDTO 저장할 디바이스 데이터
     * @return 저장된 Device 엔티티
     */
    public Device saveDeviceData(DeviceDTO deviceDTO) {
        log.info("[DeviceServiceTx] 디바이스 데이터 저장 중: {}", deviceDTO);

        Device device = Device.builder()
                .deviceId(deviceDTO.getDeviceId())
                .temperature(deviceDTO.getTemperature())
                .pressure(deviceDTO.getPressure())
                .workingTime(deviceDTO.getWorkingTime())
                .deviceStatus(deviceDTO.getDeviceStatus())
                .timeStamp(deviceDTO.getTimeStamp())
                .build();

        Device savedDevice = deviceRepository.save(device);
        log.info("[DeviceServiceTx] 디바이스 데이터가 ID: {}로 저장되었습니다", savedDevice.getDeviceSn());

        return savedDevice;
    }
}
