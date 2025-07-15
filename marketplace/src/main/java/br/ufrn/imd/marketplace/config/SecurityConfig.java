package br.ufrn.imd.marketplace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthFilter) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/usuarios/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuarios").permitAll()
                        .requestMatchers("/usuarios/verificar/**").permitAll()
                        .requestMatchers("/administradores/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/vendedores").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/vendedores/pendentes").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/produto/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/pedido/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/pedido").permitAll()
                        .requestMatchers(HttpMethod.GET, "/carrinho").permitAll()
                        .requestMatchers(HttpMethod.GET, "/carrinho/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/carrinho").permitAll()
                        .requestMatchers(HttpMethod.POST, "/carrinho/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/carrinho/produto").authenticated()
                        .requestMatchers(HttpMethod.POST, "/listaDesejos").permitAll()
                        .requestMatchers(HttpMethod.POST, "/listaDesejos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/listaDesejos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/listaDesejos/por-comprador/{compradorId}").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/listaDesejos/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/listaDesejos/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/pedido/{pedidoId}/{status}").permitAll()
                        .requestMatchers(HttpMethod.GET, "pedido/avaliacoes/produto/{produtoId}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuarios/redefinir-senha").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/carrinho/{carrinhoId}/esvaziar").permitAll()
                        .requestMatchers("/api/**").authenticated()


                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}