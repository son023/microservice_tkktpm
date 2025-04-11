package com.example.verificationservice.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "image_verifies")
public class ImageVerify {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String imageLink;
    private String label;
    private Integer eyeRecognitionModelId;
    private Integer memberId;
    private LocalDateTime timeVerify;
    private Boolean isTrue;
    private Float accuracy;
}