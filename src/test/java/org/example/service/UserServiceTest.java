package org.example.service;

import org.example.model.User;
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

        service.requestStudentRegistration("new.student@campus.local", "password123", "New Student");

        assertTrue(service.isPending("NEW.STUDENT@CAMPUS.LOCAL"));
        assertNull(service.authenticate("new.student@campus.local", "password123"));

        service.approveStudent("new.student@campus.local");

        assertNotNull(service.authenticate("new.student@campus.local", "password123"));
        assertEquals(0, service.findPendingStudents().size());
    }

    @Test
    void duplicateAndInvalidRegistrationsAreRejected() {
        UserService service = new UserService();

        assertThrows(IllegalArgumentException.class,
                () -> service.requestStudentRegistration("invalid", "password123", "Student"));
        assertThrows(IllegalArgumentException.class,
                () -> service.requestStudentRegistration("student@campus.local", "password123", "Student"));
        assertThrows(IllegalArgumentException.class,
                () -> service.requestStudentRegistration("new@campus.local", "short", "Student"));
    }
}