package org.example.web;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.annotation.PostConstruct;
import org.example.model.Report;
import org.example.model.ReportCategory;
import org.example.model.ReportStatus;
import org.example.model.UserRole;
import org.example.service.ReportService;

import java.io.IOException;
import java.util.List;

@Named("reportBean")
@RequestScoped
public class ReportBean {
    @Inject
    private ReportService reportService;

    private String title;
    private String description;
    private String location;
    private String reporter = "Etudiant demo";
    private ReportCategory category;
    private String searchText;
    private ReportStatus filterStatus;
    private ReportStatus selectedStatus;
    private long selectedId;

    @Inject
    private AuthBean authBean;

    @PostConstruct
    public void init() {
        if (authBean.getCurrentUser() != null) {
            reporter = authBean.getCurrentUser().getEmail();
        }
    }

    public List<Report> getReports() { return reportService.findAll(); }
    public List<Report> getMyReports() { return reportService.findByReporter(reporter); }
    public List<Report> getFilteredReports() { return reportService.search(searchText, filterStatus); }
    public Report getSelectedReport() { return reportService.findById(selectedId); }
    public ReportCategory[] getCategories() { return ReportCategory.values(); }
    public ReportStatus[] getStatuses() { return ReportStatus.values(); }

    public long getSignaleCount() { return countByStatus(ReportStatus.SIGNALE); }
    public long getRecuCount() { return countByStatus(ReportStatus.RECU); }
    public long getResoluCount() { return countByStatus(ReportStatus.RESOLU); }

    public long countByStatus(ReportStatus status) {
        return getReports().stream().filter(report -> report.getStatus() == status).count();
    }

    public void createReport() throws IOException {
        if (authBean.getCurrentUser() == null || authBean.getCurrentUser().getRole() != UserRole.ETUDIANT) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Action refusée", "Seul un étudiant connecté peut créer un signalement."));
            return;
        }
        reporter = authBean.getCurrentUser().getEmail();
        try {
            reportService.addReport(title, description, location, reporter, category);
        } catch (IllegalArgumentException exception) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Signalement impossible", exception.getMessage()));
            return;
        }
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Signalement cree",
                        "Le signalement a bien ete enregistre."));
        FacesContext.getCurrentInstance().getExternalContext().redirect("my-reports.xhtml");
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public ReportCategory getCategory() { return category; }
    public void setCategory(ReportCategory category) { this.category = category; }
    public String getReporter() { return reporter; }
    public void setReporter(String reporter) { this.reporter = reporter; }
    public String getSearchText() { return searchText; }
    public void setSearchText(String searchText) { this.searchText = searchText; }
    public ReportStatus getFilterStatus() { return filterStatus; }
    public void setFilterStatus(ReportStatus filterStatus) { this.filterStatus = filterStatus; }
    public ReportStatus getSelectedStatus() {
        if (selectedStatus == null && getSelectedReport() != null) {
            selectedStatus = getSelectedReport().getStatus();
        }
        return selectedStatus;
    }
    public void setSelectedStatus(ReportStatus selectedStatus) { this.selectedStatus = selectedStatus; }
    public long getSelectedId() { return selectedId; }
    public void setSelectedId(long selectedId) { this.selectedId = selectedId; }

    public void saveStatus() {
        reportService.updateStatus(selectedId, selectedStatus);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Statut mis a jour",
                        "Le signalement a ete actualise."));
    }

    public void updateStatus(long id, ReportStatus status) {
        if (authBean.getCurrentUser() == null || authBean.getCurrentUser().getRole() != UserRole.RESPONSABLE) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Action refusée", "Seul un responsable peut modifier un statut."));
            return;
        }
        reportService.updateStatus(id, status);
    }
}