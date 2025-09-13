document.addEventListener("DOMContentLoaded", function () {
    const sidebar = document.getElementById('sidebar');
    const toggleButton = document.getElementById('sidebar-toggle');
    const toggleIcon = toggleButton.querySelector('i');
    const submenuLinks = document.querySelectorAll('.sidebar-link[data-bs-toggle="collapse"]');
    const content = document.getElementById('main-content');

    let activePopover = null;

    /**
     * Habilita o deshabilita la funcionalidad de colapso de Bootstrap
     * basado en el estado del sidebar.
     */
    function manageSubmenuAttributes() {
        if (sidebar.classList.contains('collapsed')) {
            // MODO COLAPSADO: Quitamos el atributo para que solo funcione el popover.
            submenuLinks.forEach(link => {
                link.removeAttribute('data-bs-toggle');
            });
        } else {
            // MODO EXPANDIDO: Restauramos el atributo para el acordeón.
            submenuLinks.forEach(link => {
                link.setAttribute('data-bs-toggle', 'collapse');
            });
        }
    }

    /**
     * Lógica principal que se ejecuta al presionar el botón de toggle.
     */
    toggleButton.addEventListener('click', function () {
        sidebar.classList.toggle('collapsed');
        content.classList.toggle('expanded');

        // Siempre cerramos cualquier popover abierto al cambiar de estado.
        if (activePopover) {
            activePopover.dispose();
            activePopover = null;
        }

        // Si el sidebar AHORA ESTÁ COLAPSADO...
        if (sidebar.classList.contains('collapsed')) {
            // --- INICIO DE LA SOLUCIÓN DEFINITIVA ---
            // Forzamos el cierre de cualquier submenú que esté abierto.
            // En lugar de usar .hide(), quitamos la clase 'show' directamente
            // para una solución instantánea y sin conflictos.
            document.querySelectorAll('.sidebar-dropdown.collapse.show').forEach(submenu => {
                submenu.classList.remove('show');
                const parentLink = document.querySelector(`a[href="#${submenu.id}"]`);
                if (parentLink) {
                    parentLink.setAttribute('aria-expanded', 'false');
                    parentLink.classList.add('collapsed');
                }
            });
            // --- FIN DE LA SOLUCIÓN DEFINITIVA ---

            toggleIcon.classList.remove('bi-chevron-left');
            toggleIcon.classList.add('bi-chevron-right');
        } else {
            // Si el sidebar AHORA ESTÁ EXPANDIDO...
            toggleIcon.classList.remove('bi-chevron-right');
            toggleIcon.classList.add('bi-chevron-left');
        }

        // Al final, siempre actualizamos los atributos para el próximo clic.
        manageSubmenuAttributes();
    });

    /**
     * Lógica que maneja el clic en un enlace de menú con submenú.
     */
    submenuLinks.forEach(link => {
        link.addEventListener('click', function (event) {
            // Esta lógica solo se ejecuta si el sidebar está colapsado.
            if (sidebar.classList.contains('collapsed')) {
                event.preventDefault();
                event.stopPropagation();

                // Cierra el popover si se vuelve a hacer clic en el mismo ícono.
                if (activePopover && activePopover.element === this) {
                    activePopover.dispose();
                    activePopover = null;
                    return;
                }

                // Cierra cualquier otro popover que esté abierto.
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

                    // Crea y muestra el nuevo popover.
                    const popover = new bootstrap.Popover(this, {
                        container: 'body', placement: 'right', trigger: 'manual',
                        html: true, content: popoverContent, customClass: 'sidebar-popover'
                    });

                    popover.show();
                    activePopover = popover;

                    // Listener para cerrar el popover al hacer clic fuera de él.
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

    // Configuración inicial al cargar la página.
    manageSubmenuAttributes();
});