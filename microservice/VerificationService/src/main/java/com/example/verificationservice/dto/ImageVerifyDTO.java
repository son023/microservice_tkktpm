package com.example.verificationservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageVerifyDTO {
    private Integer id;

    @NotBlank(message = "Image link is required")
    private String imageLink;

    @NotBlank(message = "Label is required")
    private String label;

    @NotNull(message = "Eye recognition model ID is required")
    private Integer eyeRecognitionModelId;

    @NotNull(message = "Member ID is required")
    private Integer memberId;

    private LocalDateTime timeVerify;
    private Boolean isTrue;
    private Float accuracy;
}