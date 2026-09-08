package org.example.web;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.model.User;
import org.example.service.UserService;

import java.util.List;

@Named("userAdminBean")
@RequestScoped
public class UserAdminBean {
    @Inject
    private UserService userService;

    public List<User> getPendingStudents() {
        return userService.findPendingStudents();
    }

    public void approve(String email) {
        userService.approveStudent(email);
        addMessage("Compte validé", "L'étudiant peut maintenant se connecter.");
    }

    public void reject(String email) {
        userService.rejectStudent(email);
        addMessage("Demande refusée", "Le compte étudiant n'a pas été créé.");
    }

    private void addMessage(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail));
    }
}