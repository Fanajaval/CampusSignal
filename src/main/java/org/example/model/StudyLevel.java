package org.example.model;

public enum StudyLevel {
    L1("L1"), L2("L2"), L3("L3"), M1("M1"), M2("M2");

    private final String label;

    StudyLevel(String label) { this.label = label; }

    public String getLabel() { return label; }
}