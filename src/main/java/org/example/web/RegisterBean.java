package org.example.web;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.model.User;
import org.example.service.UserService;

import java.io.IOException;

@Named("registerBean")
@RequestScoped
public class RegisterBean {
    @Inject
    private UserService userService;
    @Inject
    private AuthBean authBean;

    private String email;
    private String password;
    private String displayName;

    public void register() throws IOException {
        if (userService.exists(email)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Inscription impossible", "Cette adresse email est déjà utilisée."));
            return;
        }
        User user = userService.registerStudent(email, password, displayName);
        authBean.login(user);
        FacesContext.getCurrentInstance().getExternalContext().redirect("student.xhtml");
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
}