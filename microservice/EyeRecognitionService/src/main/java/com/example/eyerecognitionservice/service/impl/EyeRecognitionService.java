package com.example.eyerecognitionservice.service.impl;


import com.example.eyerecognitionservice.model.EyeRecognitionModel;
import com.example.eyerecognitionservice.model.StatisticsEyeRecognitionModel;
import com.example.eyerecognitionservice.repository.EyeRecognitionModelRepository;
import com.example.eyerecognitionservice.service.IEyeRecognitionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import java.util.List;

@Service
public class EyeRecognitionService implements IEyeRecognitionService {

    private final EyeRecognitionModelRepository modelRepository;


    @Autowired
    public EyeRecognitionService(
            EyeRecognitionModelRepository modelRepository

    ) {
        this.modelRepository = modelRepository;


    }

    @Override
    public EyeRecognitionModel getModelById(Integer id) {
        return modelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Eye Recognition Model not found with id: " + id));
    }

    @Override
    public StatisticsEyeRecognitionModel getStatisticsForModel(Integer modelId, LocalDate startDate, LocalDate endDate) {
//        EyeRecognitionModel model = getModelById(eyeRecognitionModelId);
//
//
//        LocalDateTime startDateTime = startDate.atStartOfDay();
//        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
//
//        List<ImageVerify> verifications = imageVerifyRepository
//                .findByEyeRecognitionModelAndTimeVerifyBetween(model, startDateTime, endDateTime);
//
//        float totalResponseTime = 0;
//        int successCount = 0;
//
//        for (ImageVerify verify : verifications) {
//
//            if (verify.getIsTrue()) {
//                successCount++;
//            }
//        }
//
//        float accuracyRate = verifications.isEmpty() ? 0 : (float) successCount / verifications.size();
//        float averageResponseTime = 0;
//
//        EXStatisticsEyeRecognitionModelBuilder  exStatisticsEyeRecognitionModelBuilder= new EXStatisticsEyeRecognitionModelBuilder()
//                .buildDateRange(startDate, endDate)
//                .buildModelStats(model)
//                .buildAverageResponseTime(accuracyRate)
//                .buildImageVerify(verifications)
//                .buildAverageResponseTime(averageResponseTime);
//
//        StatisticsEyeRecognitionModel stats =exStatisticsEyeRecognitionModelBuilder.getResult();
//
//        return stats;

        return null;
    }

    @Override
    public List<EyeRecognitionModel> getAllModels() {
        return modelRepository.findAll();
    }



}