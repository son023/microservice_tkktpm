package com.example.memberservice.security;

import com.example.memberservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberSecurity {

    private final MemberService memberService;

    public boolean isMemberOrAdmin(Integer memberId, Authentication authentication) {
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return true;
        }

        return memberService.findByUsername(authentication.getName())
                .map(member -> member.getId().equals(memberId))
                .orElse(false);
    }
}