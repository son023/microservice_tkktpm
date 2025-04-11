package com.example.eyerecognitionservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsEyeRecognitionModel {
    private Integer id;
    private EyeRecognitionModel eyeRecognitionModel;
    private LocalDate startDate;
    private LocalDate endDate;
    private Float averageResponseTime;
    private Float accuracyRate;
}