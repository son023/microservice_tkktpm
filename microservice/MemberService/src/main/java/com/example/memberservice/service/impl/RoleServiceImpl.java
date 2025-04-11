package com.example.memberservice.service.impl;

import com.example.memberservice.entity.Role;
import com.example.memberservice.repository.RoleRepository;
import com.example.memberservice.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> findById(Integer id) {
        return roleRepository.findById(id);
    }

    @Override
    public Optional<Role> findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role update(Integer id, Role role) {
        return roleRepository.findById(id)
                .map(existingRole -> {
                    existingRole.setRoleName(role.getRoleName());
                    existingRole.setDesc(role.getDesc());
                    return roleRepository.save(existingRole);
                })
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
    }

    @Override
    public void deleteById(Integer id) {
        roleRepository.deleteById(id);
    }
}