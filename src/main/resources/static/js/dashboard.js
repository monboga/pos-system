document.addEventListener("DOMContentLoaded", function () {
    const sidebar = document.getElementById('sidebar');
    const toggleButton = document.getElementById('sidebar-toggle');
    const toggleIcon = toggleButton.querySelector('i');
    const submenuLinks = document.querySelectorAll('.sidebar-link[data-bs-toggle="collapse"]');
    const content = document.getElementById('main-content');

    let activePopover = null;

    function manageSubmenuAttributes() {
        if (sidebar.classList.contains('collapsed')) {
            submenuLinks.forEach(link => link.removeAttribute('data-bs-toggle'));
        } else {
            submenuLinks.forEach(link => link.setAttribute('data-bs-toggle', 'collapse'));
        }
    }

    // --- (NUEVA FUNCIÓN) PARA APLICAR EL ESTADO INICIAL DEL SIDEBAR ---
    function applyInitialSidebarState() {
        const savedState = localStorage.getItem('sidebarState');
        if (savedState === 'collapsed') {
            sidebar.classList.add('collapsed');
            content.classList.add('expanded');

            // Forzamos el cierre de submenús que puedan estar abiertos por defecto en la plantilla
            document.querySelectorAll('.sidebar-dropdown.collapse.show').forEach(submenu => {
                submenu.classList.remove('show');
                const parentLink = document.querySelector(`a[href="#${submenu.id}"]`);
                if (parentLink) {
                    parentLink.setAttribute('aria-expanded', 'false');
                    parentLink.classList.add('collapsed');
                }
            });

            toggleIcon.classList.remove('bi-chevron-left');
            toggleIcon.classList.add('bi-chevron-right');
        }
        // Llamamos a manageSubmenuAttributes al final para asegurar el estado correcto de los enlaces
        manageSubmenuAttributes();
    }

    toggleButton.addEventListener('click', function () {
        sidebar.classList.toggle('collapsed');
        content.classList.toggle('expanded');

        // --- (LÍNEAS AÑADIDAS) GUARDAR EL NUEVO ESTADO ---
        if (sidebar.classList.contains('collapsed')) {
            localStorage.setItem('sidebarState', 'collapsed');
        } else {
            localStorage.setItem('sidebarState', 'expanded');
        }

        if (activePopover) {
            activePopover.dispose();
            activePopover = null;
        }

        if (sidebar.classList.contains('collapsed')) {
            document.querySelectorAll('.sidebar-dropdown.collapse.show').forEach(submenu => {
                submenu.classList.remove('show');
                const parentLink = document.querySelector(`a[href="#${submenu.id}"]`);
                if (parentLink) {
                    parentLink.setAttribute('aria-expanded', 'false');
                    parentLink.classList.add('collapsed');
                }
            });
            toggleIcon.classList.remove('bi-chevron-left');
            toggleIcon.classList.add('bi-chevron-right');
        } else {
            toggleIcon.classList.remove('bi-chevron-right');
            toggleIcon.classList.add('bi-chevron-left');
        }

        manageSubmenuAttributes();
    });

    submenuLinks.forEach(link => {
        // ... (esta sección de popovers se mantiene sin cambios) ...
        link.addEventListener('click', function (event) {
            if (sidebar.classList.contains('collapsed')) {
                event.preventDefault(); event.stopPropagation();
                if (activePopover && activePopover.element === this) { activePopover.dispose(); activePopover = null; return; }
                if (activePopover) { activePopover.dispose(); }
                const targetSubmenuId = this.getAttribute('href');
                const targetSubmenu = document.querySelector(targetSubmenuId);
                if (targetSubmenu) {
                    const popoverContent = document.createElement('div');
                    Array.from(targetSubmenu.children).forEach(child => popoverContent.appendChild(child.cloneNode(true)));
                    const popover = new bootstrap.Popover(this, { container: 'body', placement: 'right', trigger: 'manual', html: true, content: popoverContent, customClass: 'sidebar-popover' });
                    popover.show(); activePopover = popover;
                    document.body.addEventListener('click', function hidePopoverOnClickOutside(e) {
                        if (!link.contains(e.target) && !document.querySelector('.popover')?.contains(e.target)) {
                            if (activePopover) { activePopover.dispose(); activePopover = null; }
                            document.body.removeEventListener('click', hidePopoverOnClickOutside);
                        }
                    }, { once: true });
                }
            }
        });
    });

    // --- LÓGICA DEL THEME SWITCHER (SIN CAMBIOS) ---
    const themeSwitch = document.getElementById('themeSwitch');
    const htmlElement = document.documentElement;

    function applyInitialTheme() {
        const savedTheme = localStorage.getItem('theme') || 'light';
        htmlElement.setAttribute('data-bs-theme', savedTheme);
        if (themeSwitch) {
            themeSwitch.checked = (savedTheme === 'dark');
        }
    }

    if (themeSwitch) {
        themeSwitch.addEventListener('change', function () {
            if (this.checked) {
                htmlElement.setAttribute('data-bs-theme', 'dark');
                localStorage.setItem('theme', 'dark');
            } else {
                htmlElement.setAttribute('data-bs-theme', 'light');
                localStorage.setItem('theme', 'light');
            }
        });
    }

    // --- (LLAMADAS INICIALES) APLICAR ESTADOS AL CARGAR LA PÁGINA ---
    applyInitialTheme();
    applyInitialSidebarState(); // <-- LLAMADA A LA NUEVA FUNCIÓN
});