document.addEventListener("DOMContentLoaded", function () {
    // Selectores del DOM para el estado vacío y el formulario
    const showBusinessFormBtn = document.getElementById('show-business-form-btn');
    const emptyStateDiv = document.getElementById('empty-business-state');
    const businessForm = document.getElementById('business-form');

    // Selectores para la previsualización de la imagen
    const logoUploadInput = document.getElementById('logoUpload');
    const logoPreview = document.getElementById('business-logo-preview');

    // Listener para el botón en el estado vacío
    if (showBusinessFormBtn) {
        showBusinessFormBtn.addEventListener('click', function () {
            if (emptyStateDiv) {
                emptyStateDiv.classList.add('d-none'); // Oculta el mensaje de bienvenida
            }
            if (businessForm) {
                businessForm.classList.remove('d-none'); // Muestra el formulario
            }
        });
    }

    // Lógica de previsualización de imagen
    if (logoUploadInput && logoPreview) {
        logoUploadInput.addEventListener('change', function (event) {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();

                // Cuando el lector termine, pone el resultado como src de la imagen de vista previa
                reader.onload = function (e) {
                    logoPreview.src = e.target.result;
                };

                // Lee el archivo para activar el evento 'onload'
                reader.readAsDataURL(file);
            }
        });
    }
});