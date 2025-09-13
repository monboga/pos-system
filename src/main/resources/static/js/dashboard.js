document.addEventListener("DOMContentLoaded", function () {
    const sidebar = document.getElementById('sidebar');
    const toggleButton = document.getElementById('sidebar-toggle');
    const toggleIcon = toggleButton.querySelector('i');
    const submenuLinks = document.querySelectorAll('.sidebar-link[data-bs-toggle="collapse"]');
    const content = document.getElementById('main-content');

    let activePopover = null;

    function manageSubmenuAttributes() {
        if (sidebar.classList.contains('collapsed')) {
            submenuLinks.forEach(link => {
                link.removeAttribute('data-bs-toggle');
            });
        } else {
            submenuLinks.forEach(link => {
                link.setAttribute('data-bs-toggle', 'collapse');
            });
        }
    }

    toggleButton.addEventListener('click', function () {
        sidebar.classList.toggle('collapsed');
        content.classList.toggle('expanded');

        if (activePopover) {
            activePopover.dispose();
            activePopover = null;
        }

        manageSubmenuAttributes();

        if (sidebar.classList.contains('collapsed')) {
            toggleIcon.classList.remove('bi-chevron-left');
            toggleIcon.classList.add('bi-chevron-right');

            // --- (LÓGICA AÑADIDA PARA EL ARREGLO) ---
            // Cierra cualquier submenú que esté abierto al contraer el sidebar.
            document.querySelectorAll('.sidebar-dropdown.collapse.show').forEach(submenu => {
                const bsCollapse = bootstrap.Collapse.getInstance(submenu);
                if (bsCollapse) {
                    bsCollapse.hide();
                }
            });
            // --- FIN DEL ARREGLO ---

        } else {
            toggleIcon.classList.remove('bi-chevron-right');
            toggleIcon.classList.add('bi-chevron-left');
        }
    });

    submenuLinks.forEach(link => {
        link.addEventListener('click', function (event) {
            if (sidebar.classList.contains('collapsed')) {
                event.preventDefault();
                event.stopPropagation();

                if (activePopover && activePopover.element === this) {
                    activePopover.dispose();
                    activePopover = null;
                    return;
                }

                if (activePopover) {
                    activePopover.dispose();
                }

                const targetSubmenuId = this.getAttribute('href');
                const targetSubmenu = document.querySelector(targetSubmenuId);

                if (targetSubmenu) {
                    const popoverContent = document.createElement('div');
                    Array.from(targetSubmenu.children).forEach(child => {
                        popoverContent.appendChild(child.cloneNode(true));
                    });

                    const popover = new bootstrap.Popover(this, {
                        container: 'body',
                        placement: 'right',
                        trigger: 'manual',
                        html: true,
                        content: popoverContent,
                        customClass: 'sidebar-popover'
                    });

                    popover.show();
                    activePopover = popover;

                    document.body.addEventListener('click', function hidePopoverOnClickOutside(e) {
                        if (!link.contains(e.target) && !document.querySelector('.popover')?.contains(e.target)) {
                            if (activePopover) {
                                activePopover.dispose();
                                activePopover = null;
                            }
                            document.body.removeEventListener('click', hidePopoverOnClickOutside);
                        }
                    }, { once: true });
                }
            }
        });
    });

    manageSubmenuAttributes();
});