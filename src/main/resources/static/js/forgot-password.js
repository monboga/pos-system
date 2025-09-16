document.addEventListener("DOMContentLoaded", function () {
    // Seleccionamos el formulario de la página
    const forgotPasswordForm = document.getElementById('forgotPasswordForm');

    if (forgotPasswordForm) {
        // Escuchamos el evento 'submit' del formulario
        forgotPasswordForm.addEventListener('submit', function () {
            // Buscamos el botón dentro del formulario
            const submitButton = forgotPasswordForm.querySelector('button[type="submit"]');

            // Deshabilitamos el botón para evitar clics múltiples
            submitButton.disabled = true;

            // Reemplazamos el texto del botón con un spinner de Bootstrap
            submitButton.innerHTML = `
                <span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                Enviando...
            `;
        });
    }
});