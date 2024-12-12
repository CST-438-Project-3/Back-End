package group15.pantrypal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
                    "http://localhost:3000", // React frontend
                    "http://localhost:19006", // Expo 
                    "http://localhost:8081", 
                    "https://pantrypal15-1175d47ce25d.herokuapp.com/", 
                    "https://pantrypal-dlxfpxjfr-xeahhs-projects.vercel.app" 
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true); // Allow credentials (cookies, sessions)
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/auth/**", // Allow all auth-related endpoints
                    "/api/oauth2/**" // Allow OAuth endpoints
                ).permitAll()
                .anyRequest().authenticated() // Protect all other endpoints
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("http://localhost:8081/logIn") // Redirect to React frontend for OAuth login
                .defaultSuccessUrl("http://localhost:8081/dashboard", true) // Redirect to React dashboard on success
                .failureUrl("http://localhost:8081/logIn?error=true") // Redirect to React login page on failure
            )
            .cors(withDefaults()) // Enable CORS
            .csrf(AbstractHttpConfigurer::disable); // Disable CSRF for simplicity (enable in production)
        return http.build();
    }
}
