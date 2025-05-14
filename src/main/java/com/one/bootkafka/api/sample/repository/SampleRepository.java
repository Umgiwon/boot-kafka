package com.one.bootkafka.api.sample.repository;

import com.one.bootkafka.api.sample.domain.entity.Sample;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleRepository extends JpaRepository<Sample, Long> {
}
