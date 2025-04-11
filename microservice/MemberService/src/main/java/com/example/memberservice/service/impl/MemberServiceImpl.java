package com.example.memberservice.service.impl;
import com.example.memberservice.dto.MemberDTO;
import com.example.memberservice.entity.Member;
import com.example.memberservice.entity.Role;
import com.example.memberservice.repository.MemberRepository;
import com.example.memberservice.repository.RoleRepository;
import com.example.memberservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @Override
    public Optional<Member> findById(Integer id) {
        return memberRepository.findById(id);
    }

    @Override
    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public Member save(MemberDTO memberDTO) {
        Member member = new Member();
        member.setUsername(memberDTO.getUsername());
        member.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
        member.setFullName(memberDTO.getFullName());
        member.setDepartment(memberDTO.getDepartment());
        member.setEmail(memberDTO.getEmail());
        member.setDienThoai(memberDTO.getDienThoai());

        Set<Role> roles = new HashSet<>();
        if (memberDTO.getRoles() != null && !memberDTO.getRoles().isEmpty()) {
            memberDTO.getRoles().forEach(roleName -> {
                roleRepository.findByRoleName(roleName)
                        .ifPresent(roles::add);
            });
        } else {
            // Mặc định là USER role
            roleRepository.findByRoleName("USER")
                    .ifPresent(roles::add);
        }
        member.setRoles(roles);

        return memberRepository.save(member);
    }

    @Override
    @Transactional
    public Member update(Integer id, MemberDTO memberDTO) {
        return memberRepository.findById(id)
                .map(existingMember -> {
                    existingMember.setFullName(memberDTO.getFullName());
                    existingMember.setDepartment(memberDTO.getDepartment());
                    existingMember.setEmail(memberDTO.getEmail());
                    existingMember.setDienThoai(memberDTO.getDienThoai());

                    if (memberDTO.getPassword() != null && !memberDTO.getPassword().isEmpty()) {
                        existingMember.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
                    }

                    if (memberDTO.getRoles() != null && !memberDTO.getRoles().isEmpty()) {
                        Set<Role> roles = new HashSet<>();
                        memberDTO.getRoles().forEach(roleName -> {
                            roleRepository.findByRoleName(roleName)
                                    .ifPresent(roles::add);
                        });
                        existingMember.setRoles(roles);
                    }

                    return memberRepository.save(existingMember);
                })
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + id));
    }

    @Override
    public void deleteById(Integer id) {
        memberRepository.deleteById(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return memberRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

}