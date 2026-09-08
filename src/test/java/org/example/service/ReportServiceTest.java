package org.example.service;

import org.example.model.ReportCategory;
import org.example.model.ReportStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReportServiceTest {
    @Test
    void validReportStartsAsSignaleAndCanBeUpdated() {
        ReportService service = new ReportService();

        var report = service.addReport("Porte bloquee", "La porte principale reste bloquee.",
                "Batiment C", "student@campus.local", ReportCategory.SECURITE);

        assertEquals(ReportStatus.SIGNALE, report.getStatus());
        service.updateStatus(report.getId(), ReportStatus.RECU);
        assertEquals(ReportStatus.RECU, service.findById(report.getId()).getStatus());
    }

    @Test
    void incompleteReportIsRejected() {
        ReportService service = new ReportService();

        assertThrows(IllegalArgumentException.class,
                () -> service.addReport("Court", "Court", "Batiment A",
                        "student@campus.local", ReportCategory.AUTRE));
    }
}