package com.NaukriChowk.Job_Wala.service;

import com.NaukriChowk.Job_Wala.model.Role;
import com.NaukriChowk.Job_Wala.model.RoleName;
import com.NaukriChowk.Job_Wala.repo.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class InitialDataLoader {

    @Autowired
    private RoleRepository roleRepository;

    @Bean
    public ApplicationRunner initializer() {
        List<RoleName> roles = Arrays.asList(RoleName.ADMIN,  RoleName.USER);
        return args -> roles.forEach(i -> createRoleIfNotFound(i));
    }

    private Optional<Role> createRoleIfNotFound(RoleName roleName) {
        Optional<Role> role = roleRepository.findByName(roleName);
        if (!role.isPresent()) {
            Role newRole = new Role();
            newRole.setName(roleName);
            newRole = roleRepository.save(newRole);
        }
        return role;
    }
}
