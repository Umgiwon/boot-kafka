package com.one.bootkafka.api.device.repository;

import com.one.bootkafka.api.device.domain.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Long> {
}
