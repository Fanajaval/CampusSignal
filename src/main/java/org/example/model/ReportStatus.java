package org.example.model;

public enum ReportStatus {
    SIGNALE("Signalé"), RECU("Reçu"), RESOLU("Résolu");

    private final String label;

    ReportStatus(String label) { this.label = label; }

    public String getLabel() { return label; }
}