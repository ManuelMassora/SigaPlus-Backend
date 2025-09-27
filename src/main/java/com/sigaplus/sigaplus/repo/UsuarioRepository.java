package com.sigaplus.sigaplus.repo;

import com.sigaplus.sigaplus.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    @Query("SELECT u FROM Usuario u JOIN u.roles r WHERE r.name = :rolename")
    Optional<Usuario> findFirstByRoles_Name(@Param("rolename") String roleName);
}