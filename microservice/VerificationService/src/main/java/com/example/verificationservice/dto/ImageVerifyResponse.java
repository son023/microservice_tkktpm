package com.example.verificationservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageVerifyResponse {
    private Integer id;
    private String imageLink;
    private String label;
    private Integer eyeRecognitionModelId;
    private String eyeRecognitionModelName;
    private Integer memberId;
    private String memberName;
    private LocalDateTime timeVerify;
    private Boolean isTrue;
    private Float accuracy;
}