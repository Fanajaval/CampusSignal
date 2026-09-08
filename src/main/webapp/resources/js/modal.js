// Modal system
(function() {
    'use strict';

    function openModal(modalId) {
        const backdrop = document.getElementById('modal-backdrop');
        const container = document.getElementById(modalId);
        
        if (backdrop && container) {
            backdrop.classList.add('active');
            container.classList.add('active');
            document.body.style.overflow = 'hidden';
        }
    }

    function closeModal(modalId) {
        const backdrop = document.getElementById('modal-backdrop');
        const container = document.getElementById(modalId);
        
        if (backdrop && container) {
            backdrop.classList.remove('active');
            container.classList.remove('active');
            document.body.style.overflow = '';
        }
    }

    function closeAllModals() {
        const backdrop = document.getElementById('modal-backdrop');
        const modals = document.querySelectorAll('.modal-container');
        
        if (backdrop) {
            backdrop.classList.remove('active');
        }
        
        modals.forEach(modal => {
            modal.classList.remove('active');
        });
        
        document.body.style.overflow = '';
    }

    // Global functions
    window.openModal = openModal;
    window.closeModal = closeModal;
    window.closeAllModals = closeAllModals;

    // Close on backdrop click
    document.addEventListener('click', function(e) {
        if (e.target.classList.contains('modal-backdrop') || 
            e.target.classList.contains('modal-container')) {
            closeAllModals();
        }
    });

    // Close on ESC key
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            closeAllModals();
        }
    });
})();
