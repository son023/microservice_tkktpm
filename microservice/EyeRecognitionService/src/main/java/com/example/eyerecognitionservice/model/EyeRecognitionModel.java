package com.example.eyerecognitionservice.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "eye_recognition_models")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EyeRecognitionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer trainSize;
    private Integer testSize;
    private Float accuracy;

}