// Toast notification system
(function() {
    'use strict';

    // Initialize toast container
    function initToastContainer() {
        let container = document.querySelector('.toast-container');
        if (!container) {
            container = document.createElement('div');
            container.className = 'toast-container';
            document.body.appendChild(container);
        }
        return container;
    }

    // Show toast notification
    function showToast(message, severity, detail) {
        const container = initToastContainer();
        
        // Create toast element
        const toast = document.createElement('div');
        toast.className = `toast toast-${severity}`;
        
        // Toast content
        const content = document.createElement('div');
        content.className = 'toast-copy';
        
        const strong = document.createElement('strong');
        strong.textContent = message;
        content.appendChild(strong);
        
        if (detail) {
            const small = document.createElement('small');
            small.textContent = detail;
            content.appendChild(small);
        }
        
        // Close button
        const closeBtn = document.createElement('button');
        closeBtn.className = 'toast-close';
        closeBtn.innerHTML = '×';
        closeBtn.setAttribute('aria-label', 'Fermer');
        closeBtn.onclick = () => removeToast(toast);
        
        toast.appendChild(content);
        toast.appendChild(closeBtn);
        container.appendChild(toast);
        
        // Trigger animation
        setTimeout(() => toast.classList.add('toast-visible'), 10);
        
        // Auto-remove after 5 seconds
        setTimeout(() => removeToast(toast), 5000);
    }

    // Remove toast with animation
    function removeToast(toast) {
        toast.classList.remove('toast-visible');
        setTimeout(() => {
            if (toast.parentNode) {
                toast.parentNode.removeChild(toast);
            }
        }, 300);
    }

    // Group validation errors into one message
    function groupValidationErrors(messages) {
        const errors = [];
        const warnings = [];
        const infos = [];
        const successes = [];
        
        messages.forEach(msg => {
            const severity = msg.severity || 'info';
            const text = msg.summary || msg.text;
            
            if (severity === 'error') {
                // Check if it's a validation error (required field)
                if (text.includes('obligatoire') || text.includes('required')) {
                    errors.push(text);
                } else {
                    // Non-validation error, keep separate
                    showToast(text, severity, msg.detail);
                }
            } else if (severity === 'warning') {
                warnings.push({ summary: text, detail: msg.detail });
            } else if (severity === 'success') {
                successes.push({ summary: text, detail: msg.detail });
            } else {
                infos.push({ summary: text, detail: msg.detail });
            }
        });
        
        // Show grouped validation errors
        if (errors.length > 0) {
            if (errors.length === 1) {
                showToast(errors[0], 'error', null);
            } else {
                showToast(
                    'Veuillez remplir tous les champs obligatoires',
                    'error',
                    errors.length + ' champ(s) requis'
                );
            }
        }
        
        // Show other message types
        warnings.forEach(w => showToast(w.summary, 'warning', w.detail));
        infos.forEach(i => showToast(i.summary, 'info', i.detail));
        successes.forEach(s => showToast(s.summary, 'success', s.detail));
    }

    // Convert JSF messages to toasts
    function convertJSFMessages() {
        const allMessages = [];
        
        // Find all JSF message elements
        const messageContainers = document.querySelectorAll('.messages, [class*="message"]');
        
        messageContainers.forEach(container => {
            const messages = container.querySelectorAll('li, .message-item, [role="alert"]');
            
            messages.forEach(msg => {
                let severity = 'info';
                let summary = '';
                let detail = '';
                
                // Detect severity from classes
                if (msg.className.includes('error') || container.className.includes('error')) {
                    severity = 'error';
                } else if (msg.className.includes('warn')) {
                    severity = 'warning';
                } else if (msg.className.includes('info')) {
                    severity = 'info';
                } else if (msg.className.includes('success')) {
                    severity = 'success';
                }
                
                // Extract message text
                const summaryEl = msg.querySelector('strong, .summary');
                const detailEl = msg.querySelector('small, .detail');
                
                if (summaryEl) {
                    summary = summaryEl.textContent.trim();
                    if (detailEl) {
                        detail = detailEl.textContent.trim();
                    }
                } else {
                    // Full text as summary
                    summary = msg.textContent.trim();
                }
                
                if (summary) {
                    allMessages.push({ severity, summary, detail });
                }
            });
            
            // Hide original message container
            if (container) {
                container.style.display = 'none';
            }
        });
        
        // Group and show messages
        if (allMessages.length > 0) {
            groupValidationErrors(allMessages);
        }
    }

    // Global function to show toast from outside
    window.showToast = showToast;

    // Auto-convert messages on page load and after AJAX
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', convertJSFMessages);
    } else {
        convertJSFMessages();
    }

    // Listen for JSF AJAX events
    if (window.jsf && jsf.ajax) {
        jsf.ajax.addOnEvent(function(data) {
            if (data.status === 'success') {
                setTimeout(convertJSFMessages, 100);
            }
        });
    }
})();
