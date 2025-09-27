package com.sigaplus.sigaplus.repo;

import com.sigaplus.sigaplus.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String nome);
}