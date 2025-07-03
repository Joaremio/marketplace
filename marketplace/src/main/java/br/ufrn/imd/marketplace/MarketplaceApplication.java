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

    // ADICIONE ESTE MÉTODO @BEAN DENTRO DA SUA CLASSE PRINCIPAL
    @Bean
    CommandLineRunner commandLineRunner(UsuarioService usuarioService, AdministradorService administradorService) {
        return args -> {
            // 1. Defina os dados do seu administrador padrão
            String adminEmail = "admin@marketplace.com";
            String adminPassword = "admin123";

            // 2. Verifique se o usuário admin já existe para não criá-lo duas vezes
            if (!usuarioService.existePorEmail(adminEmail)) {
                System.out.println(">>> Criando administrador inicial...");

                // 3. Crie um novo objeto Usuario
                Usuario admin = new Usuario();
                admin.setNome("Admin Principal");
                admin.setEmail(adminEmail);
                admin.setSenha(adminPassword); // O UsuarioService vai criptografar isso
                admin.setCpf("00000000000");     // Use um CPF fictício
                admin.setTelefone("00000000000"); // Use um telefone fictício

                // 4. Cadastre o usuário na tabela 'usuario'
                Usuario adminSalvo = usuarioService.cadastrarUsuario(admin);

                // 5. Promova este usuário a administrador, inserindo na tabela 'administrador'
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