package com.sigaplus.sigaplus.repo;

import com.sigaplus.sigaplus.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String nome);
    Optional<Role> findFirstByName(String nome);
}