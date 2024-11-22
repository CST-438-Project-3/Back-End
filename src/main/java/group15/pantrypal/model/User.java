package group15.pantrypal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "userTable")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "username", nullable = false, unique = true)
    @NotNull
    @Size(min = 3, max = 50)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    @NotNull
    @Email
    private String email;

    @Column(name = "is_admin", nullable = false)
    @NotNull
    private Boolean isAdmin;

    @Column(name = "password", nullable = false)
    @NotNull
    @Size(min = 8) // Minimum password length
    private String password;

    // Default constructor
    public User() {}

    // Getters and Setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
    }

    public void setIsAdmin(Boolean isAdmin) {
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }
}
