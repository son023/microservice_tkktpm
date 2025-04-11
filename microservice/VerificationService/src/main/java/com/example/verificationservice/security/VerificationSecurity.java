package com.example.verificationservice.security;

import com.example.verificationservice.client.MemberServiceClient;
import com.example.verificationservice.config.SecurityUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class VerificationSecurity {

    private final MemberServiceClient memberServiceClient;
    private final SecurityUtil securityUtil;
    private final ObjectMapper objectMapper;

    public boolean isMemberOrAdmin(Integer memberId, Authentication authentication) {
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return true;
        }

        String bearerToken = securityUtil.getBearerToken();
        String username = authentication.getName();

        try {
            Object memberResponse = memberServiceClient.getMemberById(bearerToken, memberId);
            JsonNode memberNode = objectMapper.convertValue(memberResponse, JsonNode.class);
            String memberUsername = memberNode.get("username").asText();

            return username.equals(memberUsername);
        } catch (Exception e) {
            log.error("Error checking member permission: {}", e.getMessage());
            return false;
        }
    }
}