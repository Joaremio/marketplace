package br.ufrn.imd.marketplace;

import br.ufrn.imd.marketplace.model.Usuario;
import br.ufrn.imd.marketplace.service.AdministradorService;
import br.ufrn.imd.marketplace.service.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MarketplaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarketplaceApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(UsuarioService usuarioService, AdministradorService administradorService) {
        return args -> {
            String adminEmail = "admin@marketplace.com";
            String adminPassword = "admin123";

            if (!usuarioService.existePorEmail(adminEmail)) {
                System.out.println(">>> Criando administrador inicial...");

                Usuario admin = new Usuario();
                admin.setNome("Admin Principal");
                admin.setEmail(adminEmail);
                admin.setSenha(adminPassword);
                admin.setCpf("00000000000");
                admin.setTelefone("00000000000");
                Usuario adminSalvo = usuarioService.cadastrarUsuario(admin);
                administradorService.inserirAdministrador(adminSalvo.getId());

                System.out.println(">>> Administrador inicial criado com sucesso!");
                System.out.println(">>> Email: " + adminEmail);
                System.out.println(">>> Senha: " + adminPassword);
            } else {
                System.out.println(">>> Administrador inicial já existe. Nenhum foi criado.");
            }
        };
    }
}