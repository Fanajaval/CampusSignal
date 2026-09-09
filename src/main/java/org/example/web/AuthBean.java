package org.example.web;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import org.example.model.User;

import java.io.IOException;
import java.io.Serializable;

@Named("authBean")
@SessionScoped
public class AuthBean implements Serializable {
    private User currentUser;

    public boolean isLoggedIn() { return currentUser != null; }
    public User getCurrentUser() { return currentUser; }
    public void login(User user) {
        currentUser = user;
        jakarta.faces.context.FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap().put("currentUser", user);
    }
    public void setCurrentUser(User user) {
        currentUser = user;
        jakarta.faces.context.FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap().put("currentUser", user);
    }

    public void logout() throws IOException {
        currentUser = null;
        var context = jakarta.faces.context.FacesContext.getCurrentInstance();
        context.getExternalContext().invalidateSession();
        context.getExternalContext().redirect("login.xhtml?logout=1");
    }
}