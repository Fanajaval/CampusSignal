package org.example.model;

import java.io.Serializable;

public class User implements Serializable {
    private String email;
    private String password;
    private String displayName;
    private String institution;
    private String studentNumber;
    private StudyLevel studyLevel;
    private UserRole role;

    public User() {
    }

    public User(String email, String password, String displayName, UserRole role) {
        this(email, password, displayName, null, null, role);
    }

    public User(String email, String password, String displayName, String institution,
                String studentNumber, UserRole role) {
        this(email, password, displayName, institution, studentNumber, null, role);
        }

        public User(String email, String password, String displayName, String institution,
            String studentNumber, StudyLevel studyLevel, UserRole role) {
        this.email = email;
        this.password = password;
        this.displayName = displayName;
        this.institution = institution;
        this.studentNumber = studentNumber;
        this.studyLevel = studyLevel;
        this.role = role;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getInstitution() { return institution; }
    public void setInstitution(String institution) { this.institution = institution; }
    public String getStudentNumber() { return studentNumber; }
    public void setStudentNumber(String studentNumber) { this.studentNumber = studentNumber; }
    public StudyLevel getStudyLevel() { return studyLevel; }
    public void setStudyLevel(StudyLevel studyLevel) { this.studyLevel = studyLevel; }
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
}