package com.example.memberservice.controller;

import com.example.memberservice.dto.AuthRequest;
import com.example.memberservice.dto.AuthResponse;
import com.example.memberservice.dto.MemberDTO;
import com.example.memberservice.dto.RegisterRequest;
import com.example.memberservice.entity.Member;
import com.example.memberservice.security.JwtUtil;
import com.example.memberservice.service.MemberService;
import com.example.memberservice.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final MemberService memberService;
    private final RoleService roleService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String jwt = jwtUtil.generateToken(userDetails);

            String roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            Member member = memberService.findByUsername(userDetails.getUsername()).orElseThrow();

            AuthResponse response = new AuthResponse(
                    jwt,
                    member.getUsername(),
                    member.getFullName(),
                    roles
            );

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        if (memberService.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        if (registerRequest.getEmail() != null && !registerRequest.getEmail().isEmpty()
                && memberService.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already in use");
        }

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setUsername(registerRequest.getUsername());
        memberDTO.setPassword(registerRequest.getPassword());
        memberDTO.setFullName(registerRequest.getFullName());
        memberDTO.setDepartment(registerRequest.getDepartment());
        memberDTO.setEmail(registerRequest.getEmail());
        memberDTO.setDienThoai(registerRequest.getDienThoai());

        Set<String> roles = new HashSet<>();
        roles.add("USER");
        memberDTO.setRoles(roles);

        Member member = memberService.save(memberDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }
}