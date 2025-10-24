package com.sigaplus.sigaplus.config;

import com.sigaplus.sigaplus.model.Role;
import com.sigaplus.sigaplus.model.Usuario;
import com.sigaplus.sigaplus.repo.RoleRepository;
import com.sigaplus.sigaplus.repo.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private final UsuarioRepository userRepo;
    private final RoleRepository roleRepo;
    private PasswordEncoder passwordEncoder;

    public AdminUserConfig(UsuarioRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepo = userRepository;
        this.roleRepo = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override

    @Transactional
    public void run(String... args) throws Exception {
        if (roleRepo.findAll().isEmpty()) {
            Role roleAdm = new Role();
            roleAdm.setName("ADMIN");
            Role roleProfissional = new Role();
            roleProfissional.setName("PROFISSIONAL");
            Role rolePsicologo = new Role();
            rolePsicologo.setName("PSICOLOGO");
            Role roleEstudante = new Role();
            roleEstudante.setName("ESTUDANTE");
            Role roleSSR = new Role();
            roleSSR.setName("SSR");
            roleRepo.save(roleAdm);
            roleRepo.save(roleProfissional);
            roleRepo.save(rolePsicologo);
            roleRepo.save(roleEstudante);
            roleRepo.save(roleSSR);
            System.out.println("Roles criados com sucesso");
        }

        var roleAdmin = roleRepo.findByName(Role.Values.ADMIN.name());
        var userAdmin = userRepo.findByEmail("admin123@email.com");

        userAdmin.ifPresentOrElse(
                user -> {
                    System.out.println("Ja existe");
                },
                () -> {
                    var user = new Usuario();
                    user.setNome("admin123");
                    user.setEmail("admin123@email.com");
                    user.setSenha(passwordEncoder.encode("123"));
                    user.setRoles(Set.of(roleAdmin));
                    userRepo.save(user);
                }
        );
    }
}