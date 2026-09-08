package org.example.web;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.model.User;
import org.example.service.UserService;

@Named("registerBean")
@RequestScoped
public class RegisterBean {
    @Inject
    private UserService userService;
    private String email;
    private String password;
    private String displayName;

    public void register() {
        try {
            userService.requestStudentRegistration(email, password, displayName);
        } catch (IllegalArgumentException exception) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Inscription impossible", exception.getMessage()));
            return;
        }
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Demande envoyée", "Votre inscription est en attente de validation par un responsable."));
        email = null;
        password = null;
        displayName = null;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
}