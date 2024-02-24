package com.NaukriChowk.Job_Wala.repo;

import com.NaukriChowk.Job_Wala.model.Role;
import com.NaukriChowk.Job_Wala.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    Optional<Role> findByName(RoleName roleName);
}
