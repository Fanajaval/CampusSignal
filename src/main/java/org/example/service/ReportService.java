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

    public List<Report> findAll() { return List.copyOf(reports); }

        public List<Report> findByReporter(String reporter) {
                return reports.stream().filter(report -> report.getReporter().equalsIgnoreCase(reporter)).toList();
        }

        public List<Report> search(String text, ReportStatus status) {
                String searchText = text == null ? "" : text.trim().toLowerCase();
                return reports.stream()
                                .filter(report -> status == null || report.getStatus() == status)
                                .filter(report -> searchText.isEmpty()
                                                || report.getTitle().toLowerCase().contains(searchText)
                                                || report.getDescription().toLowerCase().contains(searchText)
                                                || report.getLocation().toLowerCase().contains(searchText))
                                .toList();
        }

        public Report findById(long id) {
                return reports.stream().filter(report -> report.getId() == id).findFirst().orElse(null);
        }

        public void updateStatus(long id, ReportStatus status) {
                Report report = findById(id);
                if (report != null && status != null) {
                        report.setStatus(status);
                }
        }

    public Report addReport(String title, String description, String location,
                            ReportCategory category) {
                return addReport(title, description, location, "Etudiant demo", category);
        }

        public Report addReport(String title, String description, String location, String reporter,
                                                        ReportCategory category) {
        Report report = new Report(nextId++, title, description, location, reporter,
                category, ReportStatus.SIGNALE, LocalDateTime.now());
        reports.add(0, report);
        return report;
    }
}