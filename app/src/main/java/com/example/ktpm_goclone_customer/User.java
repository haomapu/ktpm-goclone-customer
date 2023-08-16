package com.example.ktpm_goclone_customer;

import java.util.HashSet;
import java.util.Set;

public class User {
    private String id;
    private String username;
    private String email;
    private String password;
    private Set<ERole> roles = new HashSet<>();
    public static User currentUser;
    public double lat, lng;
    public User() {
    }
    public User(String username, String email, String id) {
        this.username = username;
        this.email = email;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<ERole> getRoles() {
        return roles;
    }

    public void setRoles(Set<ERole> roles) {
        this.roles = roles;
    }
}