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

    // Convert JSF messages to toasts
    function convertJSFMessages() {
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
                    showToast(summary, severity, detail);
                }
            });
            
            // Hide original message container
            if (container) {
                container.style.display = 'none';
            }
        });
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
