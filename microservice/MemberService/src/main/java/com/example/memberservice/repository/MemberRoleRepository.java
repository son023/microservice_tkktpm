package com.example.memberservice.repository;

import com.example.memberservice.entity.Member;
import com.example.memberservice.entity.MemberRole;
import com.example.memberservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRoleRepository extends JpaRepository<MemberRole, Integer> {
    List<MemberRole> findByMember(Member member);
    List<MemberRole> findByRole(Role role);
    Optional<MemberRole> findByMemberAndRole(Member member, Role role);
}