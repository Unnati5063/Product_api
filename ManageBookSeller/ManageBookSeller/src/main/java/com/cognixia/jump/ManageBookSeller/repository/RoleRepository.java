package com.cognixia.jump.ManageBookSeller.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cognixia.jump.ManageBookSeller.models.Role;
import com.cognixia.jump.ManageBookSeller.models.ERole;
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}