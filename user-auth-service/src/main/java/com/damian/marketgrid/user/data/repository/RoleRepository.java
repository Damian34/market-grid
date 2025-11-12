package com.damian.marketgrid.user.data.repository;

import com.damian.marketgrid.user.data.entity.Role;
import com.damian.marketgrid.user.data.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
