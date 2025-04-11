package com.example.eyerecognitionservice.controller;

import com.example.eyerecognitionservice.config.SecurityUtil;
import com.example.eyerecognitionservice.model.EyeRecognitionModel;
import com.example.eyerecognitionservice.model.StatisticsEyeRecognitionModel;
import com.example.eyerecognitionservice.service.IEyeRecognitionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/recognition")
@RequiredArgsConstructor
@Slf4j
public class EyeRecognitionController {

    private final IEyeRecognitionService eyeRecognitionService;
    private final SecurityUtil securityUtil;

    @GetMapping("/models")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERATOR')")
    public ResponseEntity<List<EyeRecognitionModel>> getAllModels() {

        return ResponseEntity.ok(eyeRecognitionService.getAllModels());
    }

    @GetMapping("/models/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERATOR')")
    public ResponseEntity<EyeRecognitionModel> getModelById(@PathVariable Integer id) {

        return eyeRecognitionService.getModelById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/models")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EyeRecognitionModel> createModel(@RequestBody EyeRecognitionModel model) {

        EyeRecognitionModel createdModel = eyeRecognitionService.createModel(model);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdModel);
    }

    @PutMapping("/models/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EyeRecognitionModel> updateModel(@PathVariable Integer id, @RequestBody EyeRecognitionModel model) {

        EyeRecognitionModel updatedModel = eyeRecognitionService.updateModel(id, model);
        return ResponseEntity.ok(updatedModel);
    }

    @DeleteMapping("/models/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteModel(@PathVariable Integer id) {
        eyeRecognitionService.deleteModel(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/models/{id}/statistics")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERATOR')")
    public ResponseEntity<StatisticsEyeRecognitionModel> getModelStatistics(
            @PathVariable Integer id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {



        if (startDate == null) {
            startDate = LocalDate.now().minusMonths(1);
        }

        if (endDate == null) {
            endDate = LocalDate.now();
        }

        StatisticsEyeRecognitionModel statistics = eyeRecognitionService.getStatisticsForModel(id, startDate, endDate);
        return ResponseEntity.ok(statistics);
    }
}