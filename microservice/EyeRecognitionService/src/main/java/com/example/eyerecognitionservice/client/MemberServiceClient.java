package com.example.eyerecognitionservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "member-service")
public interface MemberServiceClient {

    @GetMapping("/api/members/{id}")
    Object getMemberById(@RequestHeader("Authorization") String authHeader, @PathVariable Integer id);
}