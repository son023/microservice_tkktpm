package com.example.verificationservice.service.impl;

import com.example.verificationservice.client.MemberServiceClient;
import com.example.verificationservice.client.RecognitionServiceClient;
import com.example.verificationservice.config.SecurityUtil;
import com.example.verificationservice.dto.ImageVerifyDTO;
import com.example.verificationservice.dto.ImageVerifyResponse;
import com.example.verificationservice.entity.ImageVerify;
import com.example.verificationservice.repository.ImageVerifyRepository;
import com.example.verificationservice.service.ImageVerifyService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageVerifyServiceImpl implements ImageVerifyService {

    private final ImageVerifyRepository imageVerifyRepository;
    private final MemberServiceClient memberServiceClient;
    private final RecognitionServiceClient recognitionServiceClient;
    private final SecurityUtil securityUtil;
    private final ObjectMapper objectMapper;

    @Override
    public List<ImageVerifyResponse> findAll() {
        return imageVerifyRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ImageVerifyResponse> findById(Integer id) {
        return imageVerifyRepository.findById(id)
                .map(this::convertToResponse);
    }

    @Override
    public ImageVerifyResponse save(ImageVerifyDTO imageVerifyDTO) {
        // Kiểm tra member và model có tồn tại không
        validateMemberAndModel(imageVerifyDTO.getMemberId(), imageVerifyDTO.getEyeRecognitionModelId());

        // Xử lý nhận dạng hình ảnh
        Boolean verifyResult = processImageRecognition(imageVerifyDTO);
        Float accuracy = calculateAccuracy(imageVerifyDTO);

        ImageVerify imageVerify = ImageVerify.builder()
                .imageLink(imageVerifyDTO.getImageLink())
                .label(imageVerifyDTO.getLabel())
                .eyeRecognitionModelId(imageVerifyDTO.getEyeRecognitionModelId())
                .memberId(imageVerifyDTO.getMemberId())
                .timeVerify(LocalDateTime.now())
                .isTrue(verifyResult)
                .accuracy(accuracy)
                .build();

        imageVerify = imageVerifyRepository.save(imageVerify);
        log.info("Image verification saved with ID: {}", imageVerify.getId());

        return convertToResponse(imageVerify);
    }

    @Override
    public ImageVerifyResponse update(Integer id, ImageVerifyDTO imageVerifyDTO) {
        // Kiểm tra member và model có tồn tại không
        validateMemberAndModel(imageVerifyDTO.getMemberId(), imageVerifyDTO.getEyeRecognitionModelId());

        return imageVerifyRepository.findById(id)
                .map(existingVerify -> {
                    existingVerify.setImageLink(imageVerifyDTO.getImageLink());
                    existingVerify.setLabel(imageVerifyDTO.getLabel());
                    existingVerify.setEyeRecognitionModelId(imageVerifyDTO.getEyeRecognitionModelId());
                    existingVerify.setMemberId(imageVerifyDTO.getMemberId());

                    if (imageVerifyDTO.getIsTrue() != null) {
                        existingVerify.setIsTrue(imageVerifyDTO.getIsTrue());
                    }

                    if (imageVerifyDTO.getAccuracy() != null) {
                        existingVerify.setAccuracy(imageVerifyDTO.getAccuracy());
                    }

                    ImageVerify updatedVerify = imageVerifyRepository.save(existingVerify);
                    log.info("Image verification updated with ID: {}", updatedVerify.getId());

                    return convertToResponse(updatedVerify);
                })
                .orElseThrow(() -> new RuntimeException("Image verification not found with id: " + id));
    }

    @Override
    public void deleteById(Integer id) {
        imageVerifyRepository.deleteById(id);
        log.info("Image verification deleted with ID: {}", id);
    }

    @Override
    public List<ImageVerifyResponse> findByMemberId(Integer memberId) {
        return imageVerifyRepository.findByMemberId(memberId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ImageVerifyResponse> findByModelId(Integer modelId) {
        return imageVerifyRepository.findByEyeRecognitionModelId(modelId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ImageVerifyResponse> findByTimeVerifyBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return imageVerifyRepository.findByTimeVerifyBetween(startTime, endTime).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ImageVerifyResponse> findByModelId(Integer modelId, Pageable pageable) {
        return imageVerifyRepository.findByEyeRecognitionModelId(modelId, pageable)
                .map(this::convertToResponse);
    }

    @Override
    public Page<ImageVerifyResponse> findByMemberId(Integer memberId, Pageable pageable) {
        return imageVerifyRepository.findByMemberId(memberId, pageable)
                .map(this::convertToResponse);
    }

    @Override
    public Float calculateAverageAccuracyByModel(Integer modelId) {
        return imageVerifyRepository.calculateAverageAccuracyByModel(modelId);
    }

    @Override
    public Float calculateSuccessRateByModel(Integer modelId) {
        Long successCount = imageVerifyRepository.countSuccessfulVerificationsByModel(modelId);
        Long totalCount = imageVerifyRepository.countTotalVerificationsByModel(modelId);

        if (totalCount == 0) {
            return 0.0f;
        }

        return (float) successCount / totalCount;
    }

    private ImageVerifyResponse convertToResponse(ImageVerify imageVerify) {
        String bearerToken = securityUtil.getBearerToken();

        // Lấy thông tin member
        String memberName = "Unknown";
        try {
            Object memberResponse = memberServiceClient.getMemberById(bearerToken, imageVerify.getMemberId());
            JsonNode memberNode = objectMapper.convertValue(memberResponse, JsonNode.class);
            memberName = memberNode.get("fullName").asText();
        } catch (Exception e) {
            log.error("Error fetching member info: {}", e.getMessage());
        }

        // Lấy thông tin model
        String modelName = "Unknown Model";
        try {
            Object modelResponse = recognitionServiceClient.getModelById(bearerToken, imageVerify.getEyeRecognitionModelId());
            JsonNode modelNode = objectMapper.convertValue(modelResponse, JsonNode.class);
            modelName = "Model #" + modelNode.get("id").asText() + " (Accuracy: " + modelNode.get("accuracy").asText() + ")";
        } catch (Exception e) {
            log.error("Error fetching model info: {}", e.getMessage());
        }

        return ImageVerifyResponse.builder()
                .id(imageVerify.getId())
                .imageLink(imageVerify.getImageLink())
                .label(imageVerify.getLabel())
                .eyeRecognitionModelId(imageVerify.getEyeRecognitionModelId())
                .eyeRecognitionModelName(modelName)
                .memberId(imageVerify.getMemberId())
                .memberName(memberName)
                .timeVerify(imageVerify.getTimeVerify())
                .isTrue(imageVerify.getIsTrue())
                .accuracy(imageVerify.getAccuracy())
                .build();
    }

    private void validateMemberAndModel(Integer memberId, Integer modelId) {
        String bearerToken = securityUtil.getBearerToken();

        try {
            memberServiceClient.getMemberById(bearerToken, memberId);
        } catch (Exception e) {
            throw new RuntimeException("Member not found or not accessible with ID: " + memberId);
        }

        try {
            recognitionServiceClient.getModelById(bearerToken, modelId);
        } catch (Exception e) {
            throw new RuntimeException("Eye recognition model not found or not accessible with ID: " + modelId);
        }
    }

    private Boolean processImageRecognition(ImageVerifyDTO imageVerifyDTO) {
        // Mô phỏng xử lý nhận dạng hình ảnh
        // Trong thực tế, đây sẽ là logic gọi đến model máy học
        double random = Math.random();
        return random > 0.3; // 70% xác suất nhận dạng đúng
    }

    private Float calculateAccuracy(ImageVerifyDTO imageVerifyDTO) {
        // Mô phỏng tính toán độ chính xác
        // Trong thực tế, đây sẽ là giá trị từ model máy học
        return (float) (Math.random() * 0.3 + 0.7); // Độ chính xác từ 0.7 đến 1.0
    }
}