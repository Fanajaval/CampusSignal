package org.example.web;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.model.Institution;
import org.example.model.StudyLevel;
import org.example.model.User;
import org.example.model.UserRole;
import org.example.service.UserService;

@Named("profilBean")
@RequestScoped
public class ProfilBean {
    @Inject
    private AuthBean authBean;

    @Inject
    private UserService userService;

    private String displayName;
    private String email;
    private Institution institution;
    private String studentNumber;
    private StudyLevel studyLevel;

    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;

    @PostConstruct
    public void init() {
        User current = authBean.getCurrentUser();
        if (current != null && current.getRole() == UserRole.ETUDIANT) {
            this.displayName = current.getDisplayName();
            this.email = current.getEmail();
            this.institution = current.getInstitution();
            this.studentNumber = current.getStudentNumber();
            this.studyLevel = current.getStudyLevel();
        }
    }

    public String save() {
        User current = authBean.getCurrentUser();
        if (current == null || current.getRole() != UserRole.ETUDIANT) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Action refusée", "Seul un étudiant connecté peut modifier son profil."));
            return null;
        }
        try {
            String previousEmail = current.getEmail();
            userService.updateOwnProfile(previousEmail, email, displayName, institution,
                    studentNumber, studyLevel,
                    currentPassword, newPassword, confirmNewPassword);
            User refreshed = userService.findUserByEmail(email);
            if (refreshed != null) {
                authBean.setCurrentUser(refreshed);
            }
            currentPassword = null;
            newPassword = null;
            confirmNewPassword = null;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Profil mis à jour",
                            "Vos informations ont bien été enregistrées."));
        } catch (IllegalArgumentException exception) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Mise à jour impossible", exception.getMessage()));
        }
        return null;
    }

    public Institution[] getInstitutions() { return Institution.values(); }
    public StudyLevel[] getLevels() { return StudyLevel.values(); }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Institution getInstitution() { return institution; }
    public void setInstitution(Institution institution) { this.institution = institution; }
    public String getStudentNumber() { return studentNumber; }
    public void setStudentNumber(String studentNumber) { this.studentNumber = studentNumber; }
    public StudyLevel getStudyLevel() { return studyLevel; }
    public void setStudyLevel(StudyLevel studyLevel) { this.studyLevel = studyLevel; }
    public String getCurrentPassword() { return currentPassword; }
    public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    public String getConfirmNewPassword() { return confirmNewPassword; }
    public void setConfirmNewPassword(String confirmNewPassword) { this.confirmNewPassword = confirmNewPassword; }
}
