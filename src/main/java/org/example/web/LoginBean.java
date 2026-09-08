package org.example.web;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.model.User;
import org.example.service.UserService;

import java.io.IOException;

@Named("loginBean")
@RequestScoped
public class LoginBean {
    @Inject
    private UserService userService;
    @Inject
    private AuthBean authBean;

    private String email;
    private String password;

    public void login() throws IOException {
        User user = userService.authenticate(email, password);
        if (user == null) {
            String summary = userService.isPending(email)
                ? "Validation de votre compte en cours"
                : "Impossible de vous connecter";
            String detail = userService.isPending(email)
                ? "Votre demande est toujours en attente de validation par un responsable. Veuillez patienter jusqu'à ce qu'une décision soit prise."
                : "Vérifiez votre adresse email et votre mot de passe, puis réessayez.";
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    summary, detail));
            return;
        }
        authBean.login(user);
        String destination = user.getRole().name().equals("RESPONSABLE") ? "admin.xhtml" : "dashboard.xhtml";
        FacesContext.getCurrentInstance().getExternalContext().redirect(destination);
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}