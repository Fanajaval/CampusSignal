package org.example.model;

import java.io.Serializable;

public class User implements Serializable {
    private String email;
    private String password;
    private String displayName;
    private UserRole role;

    public User() {
    }

    public User(String email, String password, String displayName, UserRole role) {
        this.email = email;
        this.password = password;
        this.displayName = displayName;
        this.role = role;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
}