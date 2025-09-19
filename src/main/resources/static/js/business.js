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
    if (logoUploadInput) {
        logoUploadInput.addEventListener('change', function (event) {
            // 1. Obtenemos el archivo que el usuario seleccionó.
            const file = event.target.files[0];

            if (file) {
                // 2. Creamos un nuevo FileReader.
                const reader = new FileReader();

                // 3. Definimos qué hacer cuando el lector termine de leer el archivo.
                reader.onload = function (e) {
                    // a. Ponemos la data de la imagen en el 'src' de nuestra etiqueta <img>.
                    logoPreviewImg.src = e.target.result;

                    // b. Forzamos que la etiqueta <img> sea visible.
                    logoPreviewImg.style.display = 'block';

                    // c. Ocultamos el placeholder del ícono.
                    if (logoPreviewPlaceholder) {
                        logoPreviewPlaceholder.style.display = 'none';
                    }
                };

                // 4. Le decimos al lector que empiece a leer el archivo.
                reader.readAsDataURL(file);
            }
        });
    }
});