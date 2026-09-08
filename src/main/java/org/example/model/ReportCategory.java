package org.example.model;

public enum ReportCategory {
    ELECTRICITE("Electricite"), EAU("Eau"), INFORMATIQUE("Informatique"),
    SECURITE("Securite"), PROPRETE("Proprete"), AUTRE("Autre");

    private final String label;

    ReportCategory(String label) { this.label = label; }

    public String getLabel() { return label; }
}