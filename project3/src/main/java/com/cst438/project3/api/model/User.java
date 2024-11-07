package com.cst438.project3.api.model;



import jakarta.persistence.*;




@Entity
@Table(name="userTable")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Integer user_id;


    @Column(name="username")
    private String username;


    @Column(name="email")
    private String email;


    @Column(name="is_admin")
    private Boolean is_admin;




    public User() {};


    public Integer getUser_id() {
        return user_id;
    }


    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
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


    public void setEmail(String email) {
        this.email = email;
    }


    public Boolean getIs_admin() {
        return is_admin;
    }


    public void setIs_admin(Boolean is_admin) {
        this.is_admin = is_admin;
    }
}
