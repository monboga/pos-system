package com.gabriel.pos_system;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gabriel.pos_system.model.RegimenFiscal;
import com.gabriel.pos_system.model.Role;
import com.gabriel.pos_system.model.User;
import com.gabriel.pos_system.repository.RegimenFiscalRepository;
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

	@Bean
	CommandLineRunner seedRegimenFiscal(RegimenFiscalRepository repository) {
		return args -> {
			// Solo ejecutar si la tabla está vacía
			if (repository.count() == 0) {
				List<RegimenFiscal> regimenes = Arrays.asList(
						new RegimenFiscal("601", "General de Ley Personas Morales", false, true),
						new RegimenFiscal("603", "Personas Morales con Fines no Lucrativos", false, true),
						new RegimenFiscal("605", "Sueldos y Salarios e Ingresos Asimilados a Salarios", true, false),
						new RegimenFiscal("606", "Arrendamiento", true, false),
						new RegimenFiscal("607", "Régimen de Enajenación o Adquisición de Bienes", true, false),
						new RegimenFiscal("608", "Demás ingresos", true, false),
						new RegimenFiscal("610", "Residentes en el Extranjero sin Establecimiento Permanente en México",
								true, true),
						new RegimenFiscal("611", "Ingresos por Dividendos (socios y accionistas)", true, false),
						new RegimenFiscal("612", "Personas Físicas con Actividades Empresariales y Profesionales", true,
								false),
						new RegimenFiscal("614", "Ingresos por intereses", true, false),
						new RegimenFiscal("615", "Régimen de los ingresos por obtención de premios", true, false),
						new RegimenFiscal("616", "Sin obligaciones fiscales", true, false),
						new RegimenFiscal("620",
								"Sociedades Cooperativas de Producción que optan por diferir sus ingresos", false,
								true),
						new RegimenFiscal("621", "Incorporación Fiscal", true, false),
						new RegimenFiscal("622", "Actividades Agrícolas, Ganaderas, Silvícolas y Pesqueras", true,
								true),
						new RegimenFiscal("623", "Opcional para Grupos de Sociedades", false, true),
						new RegimenFiscal("624", "Coordinados", true, true),
						new RegimenFiscal("625",
								"Régimen de las Actividades Empresariales con ingresos a través de Plataformas Tecnológicas",
								true, true),
						new RegimenFiscal("626", "Régimen Simplificado de Confianza", true, true));
				repository.saveAll(regimenes);
			}
		};
	}

}
