package org.example.service;

import org.example.model.Institution;
import org.example.model.User;
import org.example.model.StudyLevel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserServiceTest {
    @Test
    void newStudentMustBeApprovedBeforeLogin() {
        UserService service = new UserService();

        service.requestStudentRegistration("new.student@campus.local", "password123", "New Student",
                Institution.FACULTE_SCIENCES, "2024-EST-015", StudyLevel.L1);

        assertTrue(service.isPending("NEW.STUDENT@CAMPUS.LOCAL"));
        assertNull(service.authenticate("new.student@campus.local", "password123"));
        assertEquals(Institution.FACULTE_SCIENCES, service.findPendingStudents().get(0).getInstitution());
        assertEquals("2024-EST-015", service.findPendingStudents().get(0).getStudentNumber());
        assertEquals(StudyLevel.L1, service.findPendingStudents().get(0).getStudyLevel());

        service.approveStudent("new.student@campus.local");

        assertNotNull(service.authenticate("new.student@campus.local", "password123"));
        assertEquals(0, service.findPendingStudents().size());
    }

    @Test
    void duplicateAndInvalidRegistrationsAreRejected() {
        UserService service = new UserService();

        assertThrows(IllegalArgumentException.class,
                () -> service.requestStudentRegistration("invalid", "password123", "Student",
                    Institution.FACULTE_SCIENCES, "2024-EST-016", StudyLevel.L1));
        assertThrows(IllegalArgumentException.class,
                () -> service.requestStudentRegistration("student@campus.local", "password123", "Student",
                    Institution.ENS, "2024-EST-017", StudyLevel.L2));
        assertThrows(IllegalArgumentException.class,
                () -> service.requestStudentRegistration("new@campus.local", "short", "Student",
                    Institution.FACULTE_SCIENCES, "2024-EST-018", StudyLevel.L3));
    }
}