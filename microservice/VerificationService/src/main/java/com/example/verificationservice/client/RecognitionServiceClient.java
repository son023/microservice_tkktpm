package com.example.verificationservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "recognition-service")
public interface RecognitionServiceClient {

    @GetMapping("/api/recognition/models/{id}")
    Object getModelById(@RequestHeader("Authorization") String authHeader, @PathVariable Integer id);
}