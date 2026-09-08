package org.example.model;

public enum UserRole {
    ETUDIANT("Étudiant"),
    RESPONSABLE("Responsable");

    private final String label;

    UserRole(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}