package com.example.memberservice.service;

import com.example.memberservice.entity.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> findAll();
    Optional<Role> findById(Integer id);
    Optional<Role> findByRoleName(String roleName);
    Role save(Role role);
    Role update(Integer id, Role role);
    void deleteById(Integer id);
}