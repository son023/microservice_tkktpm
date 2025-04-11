package com.example.eyerecognitionservice.service;

import com.example.eyerecognitionservice.model.EyeRecognitionModel;
import com.example.eyerecognitionservice.model.StatisticsEyeRecognitionModel;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IEyeRecognitionService {
    List<EyeRecognitionModel> getAllModels();

    EyeRecognitionModel getModelById(Integer id);

    StatisticsEyeRecognitionModel getStatisticsForModel(Integer modelId, LocalDate startDate, LocalDate endDate);
}