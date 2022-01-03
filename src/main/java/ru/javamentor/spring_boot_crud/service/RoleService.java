package ru.javamentor.spring_boot_crud.service;

import ru.javamentor.spring_boot_crud.model.Role;

import java.util.Set;

public interface RoleService {
    Role getById(int id);

    Role getByRoleName(String roleName);

    Set<Role> findAll();

    void create(Role role);

    void update(Role role);

    void deleteById(int entityId);
}
