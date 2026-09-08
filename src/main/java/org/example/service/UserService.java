package org.example.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.example.model.User;
import org.example.model.UserRole;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class UserService {
    private final List<User> users = new ArrayList<>();
    private final List<User> pendingStudents = new ArrayList<>();

    public UserService() {
        users.add(new User("student@campus.local", "student123", "Étudiant démo", UserRole.ETUDIANT));
        users.add(new User("admin@campus.local", "admin123", "Responsable campus", UserRole.RESPONSABLE));
    }

    public synchronized User authenticate(String email, String password) {
        if (email == null || password == null) {
            return null;
        }
        String normalizedEmail = normalizeEmail(email);
        return users.stream()
                .filter(user -> user.getEmail().equals(normalizedEmail)
                        && user.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public synchronized boolean isPending(String email) {
        if (email == null) {
            return false;
        }
        return pendingStudents.stream().anyMatch(user -> user.getEmail().equals(normalizeEmail(email)));
    }

    public synchronized boolean exists(String email) {
        if (email == null) {
            return false;
        }
        String normalizedEmail = normalizeEmail(email);
        return users.stream().anyMatch(user -> user.getEmail().equals(normalizedEmail))
                || isPending(email);
    }

    public synchronized void requestStudentRegistration(String email, String password, String displayName) {
        validateRegistration(email, password, displayName);
        if (exists(email)) {
            throw new IllegalArgumentException("Cette adresse email est déjà utilisée.");
        }
        User user = new User(normalizeEmail(email), password, displayName.trim(), UserRole.ETUDIANT);
        pendingStudents.add(user);
    }

    public synchronized List<User> findPendingStudents() {
        return List.copyOf(pendingStudents);
    }

    public synchronized void approveStudent(String email) {
        User pending = findPending(email);
        if (pending != null) {
            pendingStudents.remove(pending);
            users.add(pending);
        }
    }

    public synchronized void rejectStudent(String email) {
        User pending = findPending(email);
        if (pending != null) {
            pendingStudents.remove(pending);
        }
    }

    private User findPending(String email) {
        if (email == null) {
            return null;
        }
        return pendingStudents.stream()
                .filter(user -> user.getEmail().equals(normalizeEmail(email)))
                .findFirst()
                .orElse(null);
    }

    private void validateRegistration(String email, String password, String displayName) {
        if (email == null || !email.trim().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("L'adresse email est invalide.");
        }
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 8 caractères.");
        }
        if (displayName == null || displayName.trim().length() < 2) {
            throw new IllegalArgumentException("Le nom complet est obligatoire.");
        }
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(java.util.Locale.ROOT);
    }
}