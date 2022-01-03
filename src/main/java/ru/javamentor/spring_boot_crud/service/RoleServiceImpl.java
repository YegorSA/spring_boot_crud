package ru.javamentor.spring_boot_crud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.javamentor.spring_boot_crud.model.Role;
import ru.javamentor.spring_boot_crud.repository.RoleRepository;

import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getById(int id) {
        return roleRepository.getById(id);
    }

    @Override
    public Role getByRoleName(String roleName) {
        return roleRepository.findByName(roleName);
    }

    @Override
    public Set<Role> findAll() {
        return Set.copyOf(roleRepository.findAll());
    }

    @Override
    public void create(Role role) {
        roleRepository.save(role);
    }

    @Override
    public void update(Role role) {
        roleRepository.save(role);
    }

    @Override
    public void deleteById(int roleId) {
        roleRepository.deleteById(roleId);
    }
}
