package group15.pantrypal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
public class AppConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://localhost:19006", "http://localhost:8081") // Add 8081
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // Include OPTIONS for preflight
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers( "/**").permitAll()  // Public endpoints
//                        .anyRequest().authenticated()  // Protect other endpoints
                )
                .oauth2Login(oauth2 -> oauth2  // Configure OAuth2 Login
                        .defaultSuccessUrl("/api/auth/oauth2-success", true) // Redirect after successful login
                        .failureUrl("/api/auth/oauth2-failure") // Redirect after failure
                )
                .cors(withDefaults())  // Enable CORS
                .csrf(AbstractHttpConfigurer::disable);  // Disable CSRF for simplicity (be cautious in production)
        return http.build();
    }
}
