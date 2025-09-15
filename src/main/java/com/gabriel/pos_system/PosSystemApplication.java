package com.gabriel.pos_system;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gabriel.pos_system.model.Role;
import com.gabriel.pos_system.model.User;
import com.gabriel.pos_system.repository.RoleRepository;
import com.gabriel.pos_system.repository.UserRepository;

@SpringBootApplication
public class PosSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(PosSystemApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(RoleRepository roleRepository, UserRepository userRepository,
			PasswordEncoder passwordEncoder) {
		return args -> {
			// No ejecutar si ya existen roles para evitar duplicados
			if (roleRepository.findByName("ROLE_ADMIN").isPresent()) {
				return;
			}

			// Crear Roles
			Role adminRole = roleRepository.save(new Role("ROLE_ADMIN"));
			roleRepository.save(new Role("ROLE_USER"));

			// Crear Usuario Administrador por defecto
			User admin = new User();
			admin.setFirstName("Admin");
			admin.setLastName("Usuario");
			admin.setEmail("admin@pos.com");
			// ¡IMPORTANTE! Hasheamos la contraseña antes de guardarla
			admin.setPassword(passwordEncoder.encode("admin123"));
			admin.setStatus(1);
			admin.setRoles(Set.of(adminRole));

			userRepository.save(admin);
		};
	}

}
