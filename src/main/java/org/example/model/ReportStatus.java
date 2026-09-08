package org.example.model;

public enum ReportStatus {
    SIGNALE("Signale"), EN_COURS("En cours"), RESOLU("Resolu");

    private final String label;

    ReportStatus(String label) { this.label = label; }

    public String getLabel() { return label; }
}