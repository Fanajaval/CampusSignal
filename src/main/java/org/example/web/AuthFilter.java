package org.example.web;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.User;
import org.example.model.UserRole;

import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        if (isPublic(path) || path.startsWith("/jakarta.faces.resource/")) {
            chain.doFilter(request, response);
            return;
        }

        User user = (User) httpRequest.getSession().getAttribute("currentUser");
        if (user == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.xhtml");
            return;
        }
        if ((path.equals("/admin.xhtml") || path.equals("/admin-users.xhtml") || path.equals("/admin-reports.xhtml"))
            && user.getRole() != UserRole.RESPONSABLE) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/student.xhtml");
            return;
        }
        if (path.equals("/student.xhtml") && user.getRole() != UserRole.ETUDIANT) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/admin.xhtml");
            return;
        }
        if (path.equals("/create-report.xhtml") && user.getRole() != UserRole.ETUDIANT) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/admin.xhtml");
            return;
        }
        chain.doFilter(request, response);
    }

    private boolean isPublic(String path) {
        return path.equals("/") || path.equals("/index.xhtml")
                || path.equals("/login.xhtml") || path.equals("/register.xhtml");
    }
}