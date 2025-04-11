package com.example.verificationservice.controller;



import com.example.verificationservice.config.SecurityUtil;
import com.example.verificationservice.dto.ImageVerifyDTO;
import com.example.verificationservice.dto.ImageVerifyResponse;
import com.example.verificationservice.service.ImageVerifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/verification")
@RequiredArgsConstructor
@Slf4j
public class ImageVerifyController {

    private final ImageVerifyService imageVerifyService;
    private final SecurityUtil securityUtil;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ImageVerifyResponse>> getAllVerifications() {

        return ResponseEntity.ok(imageVerifyService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ImageVerifyResponse> getVerificationById(@PathVariable Integer id) {

        return imageVerifyService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ImageVerifyResponse> createVerification(@Valid @RequestBody ImageVerifyDTO imageVerifyDTO) {

        ImageVerifyResponse createdVerification = imageVerifyService.save(imageVerifyDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVerification);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ImageVerifyResponse> updateVerification(
            @PathVariable Integer id,
            @Valid @RequestBody ImageVerifyDTO imageVerifyDTO) {

        ImageVerifyResponse updatedVerification = imageVerifyService.update(id, imageVerifyDTO);
        return ResponseEntity.ok(updatedVerification);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteVerification(@PathVariable Integer id) {

        imageVerifyService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/member/{memberId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERATOR') or @verificationSecurity.isMemberOrAdmin(#memberId, authentication)")
    public ResponseEntity<List<ImageVerifyResponse>> getVerificationsByMember(@PathVariable Integer memberId) {

        return ResponseEntity.ok(imageVerifyService.findByMemberId(memberId));
    }

    @GetMapping("/model/{modelId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERATOR')")
    public ResponseEntity<List<ImageVerifyResponse>> getVerificationsByModel(@PathVariable Integer modelId) {

        return ResponseEntity.ok(imageVerifyService.findByModelId(modelId));
    }

    @GetMapping("/time-range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERATOR')")
    public ResponseEntity<List<ImageVerifyResponse>> getVerificationsByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        return ResponseEntity.ok(imageVerifyService.findByTimeVerifyBetween(start, end));
    }

    @GetMapping("/paged/model/{modelId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERATOR')")
    public ResponseEntity<Page<ImageVerifyResponse>> getPagedVerificationsByModel(
            @PathVariable Integer modelId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("timeVerify").descending());
        return ResponseEntity.ok(imageVerifyService.findByModelId(modelId, pageRequest));
    }

    @GetMapping("/paged/member/{memberId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERATOR') or @verificationSecurity.isMemberOrAdmin(#memberId, authentication)")
    public ResponseEntity<Page<ImageVerifyResponse>> getPagedVerificationsByMember(
            @PathVariable Integer memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("timeVerify").descending());
        return ResponseEntity.ok(imageVerifyService.findByMemberId(memberId, pageRequest));
    }

    @GetMapping("/statistics/model/{modelId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERATOR')")
    public ResponseEntity<Map<String, Float>> getModelStatistics(@PathVariable Integer modelId) {
        Float avgAccuracy = imageVerifyService.calculateAverageAccuracyByModel(modelId);
        Float successRate = imageVerifyService.calculateSuccessRateByModel(modelId);

        Map<String, Float> statistics = Map.of(
                "averageAccuracy", avgAccuracy != null ? avgAccuracy : 0.0f,
                "successRate", successRate
        );

        return ResponseEntity.ok(statistics);
    }
}