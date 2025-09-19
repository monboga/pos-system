document.addEventListener("DOMContentLoaded", function () {
    // Selectores del DOM para el estado vacío y el formulario
    const showBusinessFormBtn = document.getElementById('show-business-form-btn');
    const emptyStateDiv = document.getElementById('empty-business-state');
    const businessForm = document.getElementById('business-form');

    // Selectores para la previsualización de la imagen
    const logoUploadInput = document.getElementById('logoUpload');
    const logoPreviewImg = document.getElementById('business-logo-preview');
    const logoPreviewPlaceholder = document.getElementById('business-logo-placeholder');

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
    if (logoUploadInput && logoPreviewImg && logoPreviewPlaceholder) {
        logoUploadInput.addEventListener('change', function (event) {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    logoPreviewImg.src = e.target.result;
                    logoPreviewImg.classList.remove('d-none');
                    logoPreviewPlaceholder.classList.add('d-none');
                };
                reader.readAsDataURL(file);
            }
        });
    }
});