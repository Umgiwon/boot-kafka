package com.one.bootkafka.api.sample.service;

import com.one.bootkafka.api.sample.domain.dto.request.SampleListRequestDTO;
import com.one.bootkafka.api.sample.domain.dto.response.SampleResponseDTO;
import com.one.bootkafka.api.sample.repository.SampleRepository;
import com.one.bootkafka.api.sample.repository.SampleRepositoryCustom;
import com.one.bootkafka.global.enums.common.ApiReturnCode;
import com.one.bootkafka.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SampleService {

    private final SampleRepository sampleRepository;
    private final SampleRepositoryCustom sampleRepositoryCustom;

    /**
     * Sample 단건 조회
     * @param sampleSn
     * @return
     */
    public SampleResponseDTO getSample(Long sampleSn) {
        return Optional.ofNullable(sampleRepositoryCustom.getSample(sampleSn))
                .orElseThrow(() -> new BusinessException(ApiReturnCode.NO_DATA_ERROR));
    }

    /**
     * Sample 목록 조회
     * @param dto
     * @return
     */
    public Page<SampleResponseDTO> getSampleList(SampleListRequestDTO dto, Pageable pageable) {
        return sampleRepositoryCustom.getSampleList(dto, pageable);
    }
}
