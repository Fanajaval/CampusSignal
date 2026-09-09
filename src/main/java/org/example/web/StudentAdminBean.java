package org.example.web;

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

import java.util.List;
import java.util.stream.Collectors;

@Named
@RequestScoped
public class StudentAdminBean {
    @Inject
    private UserService userService;
    
    @Inject
    private AuthBean authBean;
    
    // Form fields for add/edit
    private String email;
    private String password;
    private String displayName;
    private Institution institution;
    private String studentNumber;
    private StudyLevel studyLevel;
    
    // Edit mode
    private String editingEmail;
    private boolean editMode = false;
    
    public List<User> getApprovedStudents() {
        return userService.findApprovedUsers().stream()
            .filter(user -> user.getRole() == UserRole.ETUDIANT)
            .collect(Collectors.toList());
    }
    
    public StudyLevel[] getStudyLevels() {
        return StudyLevel.values();
    }
    
    public Institution[] getInstitutions() {
        return Institution.values();
    }
    
    public String addStudent() {
        try {
            userService.requestStudentRegistration(email, password, displayName, 
                institution, studentNumber, studyLevel);
            // Auto-approve since admin is adding
            userService.approveStudent(email);
            
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "✓ Étudiant ajouté avec succès", displayName + " a été créé."));
            clearForm();
            return null;
        } catch (IllegalArgumentException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "❌ " + e.getMessage(), null));
            return null;
        }
    }
    
    public String prepareEdit(String studentEmail) {
        try {
            User student = userService.findUserByEmail(studentEmail);
            if (student == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                        "❌ Étudiant introuvable", null));
                return null;
            }
            
            this.editingEmail = student.getEmail();
            this.email = student.getEmail();
            this.displayName = student.getDisplayName();
            this.institution = student.getInstitution();
            this.studentNumber = student.getStudentNumber();
            this.studyLevel = student.getStudyLevel();
            this.editMode = true;
            
            return null;
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "❌ Erreur lors du chargement", e.getMessage()));
            return null;
        }
    }
    
    public String updateStudent() {
        try {
            userService.updateUser(editingEmail, displayName, institution, 
                studentNumber, studyLevel);
            
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "✓ Étudiant modifié avec succès", displayName + " a été mis à jour."));
            clearForm();
            return null;
        } catch (IllegalArgumentException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "❌ " + e.getMessage(), null));
            return null;
        }
    }
    
    public String deleteStudent(String studentEmail) {
        try {
            User student = userService.findUserByEmail(studentEmail);
            if (student == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                        "❌ Étudiant introuvable", null));
                return null;
            }
            
            String studentName = student.getDisplayName();
            userService.deleteUser(studentEmail);
            
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "✓ Étudiant supprimé", studentName + " a été supprimé du système."));
            return null;
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "❌ Erreur lors de la suppression", e.getMessage()));
            return null;
        }
    }
    
    public String cancelEdit() {
        clearForm();
        return null;
    }
    
    private void clearForm() {
        email = null;
        password = null;
        displayName = null;
        institution = null;
        studentNumber = null;
        studyLevel = null;
        editMode = false;
        editingEmail = null;
    }
    
    // Getters and setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    
    public Institution getInstitution() { return institution; }
    public void setInstitution(Institution institution) { this.institution = institution; }
    
    public String getStudentNumber() { return studentNumber; }
    public void setStudentNumber(String studentNumber) { this.studentNumber = studentNumber; }
    
    public StudyLevel getStudyLevel() { return studyLevel; }
    public void setStudyLevel(StudyLevel studyLevel) { this.studyLevel = studyLevel; }
    
    public boolean isEditMode() { return editMode; }
    public String getEditingEmail() { return editingEmail; }
}
