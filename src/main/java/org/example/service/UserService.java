package org.example.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.example.model.User;
import org.example.model.UserRole;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class UserService {
    private final List<User> users = new ArrayList<>();

    public UserService() {
        users.add(new User("student@campus.local", "student123", "Étudiant démo", UserRole.ETUDIANT));
        users.add(new User("admin@campus.local", "admin123", "Responsable campus", UserRole.RESPONSABLE));
    }

    public User authenticate(String email, String password) {
        return users.stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email)
                        && user.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public boolean exists(String email) {
        return users.stream().anyMatch(user -> user.getEmail().equalsIgnoreCase(email));
    }

    public User registerStudent(String email, String password, String displayName) {
        User user = new User(email.trim().toLowerCase(), password, displayName.trim(), UserRole.ETUDIANT);
        users.add(user);
        return user;
    }
}