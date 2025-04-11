package com.example.verificationservice.service;

import com.example.verificationservice.dto.ImageVerifyDTO;
import com.example.verificationservice.dto.ImageVerifyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ImageVerifyService {
    List<ImageVerifyResponse> findAll();
    Optional<ImageVerifyResponse> findById(Integer id);
    ImageVerifyResponse save(ImageVerifyDTO imageVerifyDTO);
    ImageVerifyResponse update(Integer id, ImageVerifyDTO imageVerifyDTO);
    void deleteById(Integer id);

    List<ImageVerifyResponse> findByMemberId(Integer memberId);
    List<ImageVerifyResponse> findByModelId(Integer modelId);
    List<ImageVerifyResponse> findByTimeVerifyBetween(LocalDateTime startTime, LocalDateTime endTime);

    Page<ImageVerifyResponse> findByModelId(Integer modelId, Pageable pageable);
    Page<ImageVerifyResponse> findByMemberId(Integer memberId, Pageable pageable);

    Float calculateAverageAccuracyByModel(Integer modelId);
    Float calculateSuccessRateByModel(Integer modelId);
}