package com.example.eyerecognitionservice.repository;


import com.example.eyerecognitionservice.model.EyeRecognitionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EyeRecognitionModelRepository extends JpaRepository<EyeRecognitionModel, Integer> {
}