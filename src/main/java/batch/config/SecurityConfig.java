
/*
package batch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // désactiver CSRF pour les APIs stateless
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/batch/public/**").permitAll()  // endpoints publics
                        .requestMatchers("/api/batch/admin/**").hasRole("ADMIN") // endpoints protégés
                        .anyRequest().authenticated())
                        .oauth2Login(Customizer.withDefaults());
                        //.httpBasic(Customizer.withDefaults()); // ou .oauth2ResourceServer().jwt() pour JWT
                                                               //.oauth2Login(Customizer.withDefaults()) (redirection vers Google) → il faut client_id + client_secret.
        return http.build();
    }
}

*/
