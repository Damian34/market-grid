package com.damian.marketgrid.user.data.repository;

import com.damian.marketgrid.user.data.entity.User;
import com.damian.marketgrid.user.data.entity.UserIdentity;
import com.damian.marketgrid.user.data.projection.UserAuthDetailsProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByIdentity(UserIdentity identity);

    Optional<UserAuthDetailsProjection> findByIdentity(UserIdentity identity);
}
