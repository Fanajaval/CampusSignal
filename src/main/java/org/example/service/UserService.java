package org.example.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.example.model.User;
import org.example.model.UserRole;
import org.example.model.StudyLevel;

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

    public synchronized void requestStudentRegistration(String email, String password, String displayName,
                                                        String institution, String studentNumber,
                                                        StudyLevel studyLevel) {
        validateRegistration(email, password, displayName, institution, studentNumber, studyLevel);
        if (exists(email)) {
            throw new IllegalArgumentException("Cette adresse email est déjà utilisée.");
        }
        User user = new User(normalizeEmail(email), password, displayName.trim(), institution.trim(),
            studentNumber.trim(), studyLevel, UserRole.ETUDIANT);
        pendingStudents.add(user);
    }

    public synchronized List<User> findPendingStudents() {
        return List.copyOf(pendingStudents);
    }
    
    public synchronized List<User> findApprovedUsers() {
        return List.copyOf(users);
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
    
    public synchronized void deleteUser(String email) {
        if (email == null) {
            return;
        }
        String normalizedEmail = normalizeEmail(email);
        users.removeIf(user -> user.getEmail().equals(normalizedEmail));
    }
    
    public synchronized void updateUser(String email, String displayName, String institution, 
                                       String studentNumber, StudyLevel studyLevel) {
        if (email == null) {
            throw new IllegalArgumentException("L'email est obligatoire.");
        }
        String normalizedEmail = normalizeEmail(email);
        User user = users.stream()
            .filter(u -> u.getEmail().equals(normalizedEmail))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable."));
        
        // Validation
        if (displayName == null || displayName.trim().length() < 2) {
            throw new IllegalArgumentException("Le nom complet est obligatoire.");
        }
        if (institution == null || institution.trim().length() < 2) {
            throw new IllegalArgumentException("L'école ou la faculté est obligatoire.");
        }
        if (studentNumber == null || !studentNumber.trim().matches("^[A-Za-z0-9][A-Za-z0-9./-]{2,29}$")) {
            throw new IllegalArgumentException("Le matricule est invalide.");
        }
        if (studyLevel == null) {
            throw new IllegalArgumentException("Le niveau d'étude est obligatoire.");
        }
        
        // Update user
        user.setDisplayName(displayName.trim());
        user.setInstitution(institution.trim());
        user.setStudentNumber(studentNumber.trim());
        user.setStudyLevel(studyLevel);
    }
    
    public synchronized User findUserByEmail(String email) {
        if (email == null) {
            return null;
        }
        String normalizedEmail = normalizeEmail(email);
        return users.stream()
            .filter(u -> u.getEmail().equals(normalizedEmail))
            .findFirst()
            .orElse(null);
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

    private void validateRegistration(String email, String password, String displayName,
                                      String institution, String studentNumber, StudyLevel studyLevel) {
        if (email == null || !email.trim().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("L'adresse email est invalide.");
        }
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 8 caractères.");
        }
        if (displayName == null || displayName.trim().length() < 2) {
            throw new IllegalArgumentException("Le nom complet est obligatoire.");
        }
        if (institution == null || institution.trim().length() < 2) {
            throw new IllegalArgumentException("L'école ou la faculté est obligatoire.");
        }
        if (studentNumber == null || !studentNumber.trim().matches("^[A-Za-z0-9][A-Za-z0-9./-]{2,29}$")) {
            throw new IllegalArgumentException("Le matricule est invalide.");
        }
        if (studyLevel == null) {
            throw new IllegalArgumentException("Le niveau d'étude est obligatoire.");
        }
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(java.util.Locale.ROOT);
    }
}