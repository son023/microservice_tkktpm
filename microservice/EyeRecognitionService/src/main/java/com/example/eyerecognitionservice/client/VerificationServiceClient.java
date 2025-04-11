package com.example.eyerecognitionservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "verification-service")
public interface VerificationServiceClient {

    @GetMapping("/api/verification/{id}")
    Object getVerificationById(@RequestHeader("Authorization") String authHeader, @PathVariable Integer id);
}