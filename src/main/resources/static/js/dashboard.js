document.addEventListener("DOMContentLoaded", function () {
    // --- LÓGICA DEL SIDEBAR (SIN CAMBIOS) ---
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

    toggleButton.addEventListener('click', function () {
        sidebar.classList.toggle('collapsed');
        content.classList.toggle('expanded');

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
                    document.body.addEventListener('click', function hide(e) {
                        if (!link.contains(e.target) && !document.querySelector('.popover')?.contains(e.target)) {
                            if (activePopover) { activePopover.dispose(); activePopover = null; }
                            document.body.removeEventListener('click', hide);
                        }
                    }, { once: true });
                }
            }
        });
    });

    manageSubmenuAttributes();

    // --- (NUEVO) LÓGICA PARA EL THEME SWITCHER ---
    const themeSwitch = document.getElementById('themeSwitch');
    const htmlElement = document.documentElement;

    // Función para aplicar el tema guardado al cargar la página
    function applyInitialTheme() {
        const savedTheme = localStorage.getItem('theme') || 'light'; // 'light' por defecto
        htmlElement.setAttribute('data-bs-theme', savedTheme);
        if (themeSwitch) {
            themeSwitch.checked = (savedTheme === 'dark');
        }
    }

    // Listener para cambiar el tema cuando se hace clic en el switch
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

    // Aplicar el tema al cargar el script
    applyInitialTheme();
});