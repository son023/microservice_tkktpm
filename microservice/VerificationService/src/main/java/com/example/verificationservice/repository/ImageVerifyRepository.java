package com.example.verificationservice.repository;

import com.example.verificationservice.entity.ImageVerify;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ImageVerifyRepository extends JpaRepository<ImageVerify, Integer> {
    List<ImageVerify> findByMemberId(Integer memberId);
    List<ImageVerify> findByEyeRecognitionModelId(Integer modelId);

    @Query("SELECT iv FROM ImageVerify iv WHERE iv.timeVerify BETWEEN :startTime AND :endTime")
    List<ImageVerify> findByTimeVerifyBetween(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    @Query("SELECT iv FROM ImageVerify iv WHERE iv.eyeRecognitionModelId = :modelId AND iv.timeVerify BETWEEN :startTime AND :endTime")
    List<ImageVerify> findByModelAndTimeVerifyBetween(
            @Param("modelId") Integer modelId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    @Query("SELECT AVG(iv.accuracy) FROM ImageVerify iv WHERE iv.eyeRecognitionModelId = :modelId")
    Float calculateAverageAccuracyByModel(@Param("modelId") Integer modelId);

    @Query("SELECT COUNT(iv) FROM ImageVerify iv WHERE iv.eyeRecognitionModelId = :modelId AND iv.isTrue = true")
    Long countSuccessfulVerificationsByModel(@Param("modelId") Integer modelId);

    @Query("SELECT COUNT(iv) FROM ImageVerify iv WHERE iv.eyeRecognitionModelId = :modelId")
    Long countTotalVerificationsByModel(@Param("modelId") Integer modelId);

    Page<ImageVerify> findByEyeRecognitionModelId(Integer modelId, Pageable pageable);
    Page<ImageVerify> findByMemberId(Integer memberId, Pageable pageable);
}