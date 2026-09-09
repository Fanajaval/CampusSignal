/**
 * Report Detail Modal Handler
 * Gère l'affichage des détails des signalements dans une modal
 */

/**
 * Ouvre la modal avec les détails d'un signalement à partir de l'élément cliqué
 * @param {HTMLElement} element - L'élément qui a déclenché l'action
 */
function openReportDetailFromElement(element) {
    // Recherche du parent article.report-row
    var reportRow = element.closest('.report-row');
    if (!reportRow) {
        console.error('Report row not found');
        return;
    }
    
    // Extraction des données depuis les data attributes
    var report = {
        id: reportRow.getAttribute('data-report-id'),
        title: reportRow.getAttribute('data-report-title'),
        description: reportRow.getAttribute('data-report-description'),
        category: reportRow.getAttribute('data-report-category'),
        location: reportRow.getAttribute('data-report-location'),
        reporter: reportRow.getAttribute('data-report-reporter'),
        status: reportRow.getAttribute('data-report-status'),
        statusClass: reportRow.getAttribute('data-report-status-class'),
        updated: reportRow.getAttribute('data-report-updated') === 'true',
        createdAt: reportRow.getAttribute('data-report-created-at'),
        updatedAt: reportRow.getAttribute('data-report-updated-at'),
        receivedAt: reportRow.getAttribute('data-report-received-at'),
        resolvedAt: reportRow.getAttribute('data-report-resolved-at')
    };
    
    // Affichage de la modal
    displayReportDetail(report);
}

/**
 * Ouvre la modal avec les détails d'un signalement (version compatible avec l'ancien code)
 * @param {number} reportId - L'ID du signalement à afficher
 */
function openReportDetail(reportId) {
    // Recherche de l'élément avec cet ID
    var reportRow = document.querySelector('.report-row[data-report-id="' + reportId + '"]');
    if (!reportRow) {
        console.error('Report not found:', reportId);
        return;
    }
    
    // Extraction des données depuis les data attributes
    var report = {
        id: reportRow.getAttribute('data-report-id'),
        title: reportRow.getAttribute('data-report-title'),
        description: reportRow.getAttribute('data-report-description'),
        category: reportRow.getAttribute('data-report-category'),
        location: reportRow.getAttribute('data-report-location'),
        reporter: reportRow.getAttribute('data-report-reporter'),
        status: reportRow.getAttribute('data-report-status'),
        statusClass: reportRow.getAttribute('data-report-status-class'),
        updated: reportRow.getAttribute('data-report-updated') === 'true',
        createdAt: reportRow.getAttribute('data-report-created-at'),
        updatedAt: reportRow.getAttribute('data-report-updated-at'),
        receivedAt: reportRow.getAttribute('data-report-received-at'),
        resolvedAt: reportRow.getAttribute('data-report-resolved-at')
    };
    
    // Affichage de la modal
    displayReportDetail(report);
}

/**
 * Affiche les détails d'un signalement dans la modal
 * @param {Object} report - Les données du signalement
 */
function displayReportDetail(report) {
    // Construction du HTML
    var content = document.getElementById('report-detail-content');
    var html = '';
    
    // Header avec titre et statut
    html += '<div class="detail-header">';
    html += '<div class="detail-header-content">';
    html += '<h2>' + escapeHtml(report.title) + '</h2>';
    html += '<span class="status status-' + report.statusClass + '">' + escapeHtml(report.status) + '</span>';
    html += '</div>';
    html += '</div>';
    
    // Description
    html += '<div class="detail-description">' + escapeHtml(report.description) + '</div>';
    
    // Grid avec les métadonnées
    html += '<div class="detail-grid">';
    
    html += '<div class="detail-item">';
    html += '<span class="detail-item-label">Catégorie</span>';
    html += '<span class="detail-item-value">' + escapeHtml(report.category) + '</span>';
    html += '</div>';
    
    html += '<div class="detail-item">';
    html += '<span class="detail-item-label">Lieu</span>';
    html += '<span class="detail-item-value">' + escapeHtml(report.location) + '</span>';
    html += '</div>';
    
    html += '<div class="detail-item">';
    html += '<span class="detail-item-label">Déclarant</span>';
    html += '<span class="detail-item-value">' + escapeHtml(report.reporter) + '</span>';
    html += '</div>';
    
    html += '<div class="detail-item">';
    html += '<span class="detail-item-label">Signalement #</span>';
    html += '<span class="detail-item-value">' + report.id + '</span>';
    html += '</div>';
    
    html += '</div>';
    
    // Historique / Chronologie
    html += '<div class="detail-history-section">';
    html += '<p class="eyebrow" style="margin-bottom: 0.75rem;">Historique</p>';
    html += '<div class="report-history">';
    
    // Créé
    html += '<div class="history-item history-created">';
    html += '<span class="history-dot"></span>';
    html += '<div class="history-content">';
    html += '<span class="history-label">Créé</span>';
    html += '<span class="history-date">' + escapeHtml(report.createdAt || '—') + '</span>';
    html += '</div>';
    html += '</div>';
    
    // Modifié
    if (report.updated && report.updatedAt && report.updatedAt !== '—') {
        html += '<div class="history-item history-updated">';
        html += '<span class="history-dot"></span>';
        html += '<div class="history-content">';
        html += '<span class="history-label">Modifié par l\'étudiant</span>';
        html += '<span class="history-date">' + escapeHtml(report.updatedAt) + '</span>';
        html += '</div>';
        html += '</div>';
    }
    
    // Reçu
    if (report.receivedAt && report.receivedAt !== '—') {
        html += '<div class="history-item history-received">';
        html += '<span class="history-dot"></span>';
        html += '<div class="history-content">';
        html += '<span class="history-label">Marqué « Reçu » par l\'administration</span>';
        html += '<span class="history-date">' + escapeHtml(report.receivedAt) + '</span>';
        html += '</div>';
        html += '</div>';
    }
    
    // Résolu
    if (report.resolvedAt && report.resolvedAt !== '—') {
        html += '<div class="history-item history-resolved">';
        html += '<span class="history-dot"></span>';
        html += '<div class="history-content">';
        html += '<span class="history-label">Résolu</span>';
        html += '<span class="history-date">' + escapeHtml(report.resolvedAt) + '</span>';
        html += '</div>';
        html += '</div>';
    }
    
    html += '</div>';
    html += '</div>';
    
    content.innerHTML = html;
    
    // Ouverture de la modal
    openModal('report-detail-modal');
}

/**
 * Échappe le HTML pour éviter les injections XSS
 * @param {string} text - Le texte à échapper
 * @returns {string} Le texte échappé
 */
function escapeHtml(text) {
    if (!text) return '';
    var div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}
