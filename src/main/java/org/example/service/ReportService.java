package org.example.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.example.model.Report;
import org.example.model.ReportCategory;
import org.example.model.ReportStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ReportService {
    private final List<Report> reports = new ArrayList<>();
    private long nextId = 1;

    public ReportService() {
        addReport("Ampoule cassee", "Une ampoule ne fonctionne plus dans le couloir.",
                "Batiment A - 1er etage", "student@campus.local", ReportCategory.ELECTRICITE);
        addReport("Fuite d'eau", "De l'eau s'accumule pres des sanitaires.",
                "Batiment B - Rez-de-chaussee", "student@campus.local", ReportCategory.EAU);
        Report report = addReport("Ordinateur en panne", "Le poste ne demarre plus.",
                "Bibliotheque", "student@campus.local", ReportCategory.INFORMATIQUE);
        report.setStatus(ReportStatus.RECU);
    }

    public synchronized List<Report> findAll() { return List.copyOf(reports); }

    public synchronized List<Report> findByReporter(String reporter) {
        if (reporter == null) {
            return List.of();
        }
        return reports.stream()
                .filter(report -> report.getReporter().equalsIgnoreCase(reporter))
                .toList();
    }

    public synchronized List<Report> search(String text, ReportStatus status) {
        String searchText = text == null ? "" : text.trim().toLowerCase(java.util.Locale.ROOT);
        return reports.stream()
                .filter(report -> status == null || report.getStatus() == status)
                .filter(report -> searchText.isEmpty()
                        || report.getTitle().toLowerCase(java.util.Locale.ROOT).contains(searchText)
                        || report.getDescription().toLowerCase(java.util.Locale.ROOT).contains(searchText)
                        || report.getLocation().toLowerCase(java.util.Locale.ROOT).contains(searchText))
                .toList();
    }

    public synchronized Report findById(long id) {
        return reports.stream().filter(report -> report.getId() == id).findFirst().orElse(null);
    }

    public synchronized void updateStatus(long id, ReportStatus status) {
        Report report = findById(id);
        if (report != null && status != null) {
            report.setStatus(status);
        }
    }

        public synchronized Report addReport(String title, String description, String location,
                            ReportCategory category) {
                return addReport(title, description, location, "Etudiant demo", category);
        }

        public synchronized Report addReport(String title, String description, String location, String reporter,
                                                                                 ReportCategory category) {
        validateReport(title, description, location, reporter, category);
        Report report = new Report(nextId++, title, description, location, reporter,
                category, ReportStatus.SIGNALE, LocalDateTime.now());
        reports.add(0, report);
        return report;
    }

        private void validateReport(String title, String description, String location,
                                                                String reporter, ReportCategory category) {
                if (isBlank(title) || title.trim().length() < 5) {
                        throw new IllegalArgumentException("Le titre doit contenir au moins 5 caractères.");
                }
                if (isBlank(description) || description.trim().length() < 10) {
                        throw new IllegalArgumentException("La description doit contenir au moins 10 caractères.");
                }
                if (isBlank(location)) {
                        throw new IllegalArgumentException("Le lieu est obligatoire.");
                }
                if (isBlank(reporter)) {
                        throw new IllegalArgumentException("Le déclarant est obligatoire.");
                }
                if (category == null) {
                        throw new IllegalArgumentException("La catégorie est obligatoire.");
                }
        }

        private boolean isBlank(String value) {
                return value == null || value.trim().isEmpty();
        }
}