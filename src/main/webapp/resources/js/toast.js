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
        const validationErrors = [];
        const otherMessages = [];
        
        messages.forEach(msg => {
            const text = msg.summary || msg.text || '';
            
            // Check if it's a validation error
            if (text.includes('obligatoire') || text.includes('required') || text.includes('requis')) {
                validationErrors.push(text);
            } else {
                otherMessages.push(msg);
            }
        });
        
        // Show grouped validation errors as ONE toast
        if (validationErrors.length > 0) {
            if (validationErrors.length === 1) {
                showToast(validationErrors[0], 'error', null);
            } else {
                showToast(
                    'Veuillez remplir tous les champs obligatoires',
                    'error',
                    validationErrors.length + ' champ(s) requis'
                );
            }
        }
        
        // Show other messages separately
        otherMessages.forEach(msg => {
            showToast(msg.summary || msg.text, msg.severity, msg.detail);
        });
    }

    // Convert JSF messages to toasts
    function convertJSFMessages() {
        const allMessages = [];
        
        // Find all message containers
        const messageContainers = document.querySelectorAll('.messages, ul.messages, div.messages');
        
        messageContainers.forEach(container => {
            // Try to find list items
            let messages = container.querySelectorAll('li');
            
            // If no list items, try other formats
            if (messages.length === 0) {
                messages = container.querySelectorAll('.ui-messages-error, .ui-messages-info, .ui-messages-warn');
            }
            
            messages.forEach(msg => {
                let severity = 'info';
                let text = msg.textContent.trim();
                
                // Remove the × character if present
                text = text.replace(/×/g, '').trim();
                
                // Skip empty messages
                if (!text || text.length === 0) return;
                
                // Detect severity
                if (msg.classList.contains('ui-messages-error') || 
                    msg.classList.contains('error') ||
                    container.classList.contains('error')) {
                    severity = 'error';
                } else if (msg.classList.contains('ui-messages-warn') || msg.classList.contains('warn')) {
                    severity = 'warning';
                } else if (msg.classList.contains('ui-messages-info') || msg.classList.contains('info')) {
                    severity = 'info';
                } else if (msg.classList.contains('ui-messages-success') || msg.classList.contains('success')) {
                    severity = 'success';
                }
                
                allMessages.push({
                    text: text,
                    summary: text,
                    severity: severity,
                    detail: null
                });
            });
            
            // Hide the original container
            container.style.display = 'none';
        });
        
        // Group and show messages
        if (allMessages.length > 0) {
            groupValidationErrors(allMessages);
        }
    }

    // Global function
    window.showToast = showToast;

    // Run on page load
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', convertJSFMessages);
    } else {
        convertJSFMessages();
    }
    
    // Also run after a short delay to catch late-rendered messages
    setTimeout(convertJSFMessages, 100);

    // Listen for JSF AJAX events
    if (typeof jsf !== 'undefined' && jsf.ajax) {
        jsf.ajax.addOnEvent(function(data) {
            if (data.status === 'success') {
                setTimeout(convertJSFMessages, 150);
            }
        });
    }
})();
