document.addEventListener("DOMContentLoaded", function () {
    const sidebar = document.getElementById('sidebar');
    const content = document.getElementById('main-content');
    const toggleButton = document.getElementById('sidebar-toggle');
    const toggleIcon = toggleButton.querySelector('i'); // Seleccionamos el ícono

    toggleButton.addEventListener('click', function () {
        sidebar.classList.toggle('collapsed');
        content.classList.toggle('expanded');

        // Cambiar el ícono de la flecha
        if (sidebar.classList.contains('collapsed')) {
            toggleIcon.classList.remove('bi-chevron-left');
            toggleIcon.classList.add('bi-chevron-right');
        } else {
            toggleIcon.classList.remove('bi-chevron-right');
            toggleIcon.classList.add('bi-chevron-left');
        }
    });
});