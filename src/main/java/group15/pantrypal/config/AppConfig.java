package group15.pantrypal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class AppConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:3000",
                        "http://localhost:19006",
                        "http://localhost:8081",
                        "https://pantrypal15-1175d47ce25d.herokuapp.com/"
                ) // Add all allowed origins
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // Include OPTIONS for preflight
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**", // Allow all auth-related endpoints
                                "/api/oauth2/**" // Allow OAuth endpoints
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("http://localhost:8081/logIn") // Redirect to React frontend for OAuth login
                        .defaultSuccessUrl("http://localhost:8081/dashboard", true) // Redirect to React dashboard on success
                        .failureUrl("http://localhost:8081/logIn?error=true") // Redirect on failure
                )
                .cors(withDefaults()) // Enable CORS
                .csrf(AbstractHttpConfigurer::disable); // Disable CSRF for simplicity (enable in production)
        return http.build();
    }

}
