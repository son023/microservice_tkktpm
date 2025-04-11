package com.example.memberservice.service;


import com.example.memberservice.dto.MemberDTO;
import com.example.memberservice.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    List<Member> findAll();
    Optional<Member> findById(Integer id);
    Optional<Member> findByUsername(String username);
    Member save(MemberDTO memberDTO);
    Member update(Integer id, MemberDTO memberDTO);
    void deleteById(Integer id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}