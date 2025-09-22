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

    function showErrorToast(message) {
        const toastContainer = document.querySelector('.toast-container');
        if (!toastContainer) return;
        const toastHtml = `
            <div class="toast" role="alert" aria-live="assertive" aria-atomic="true">
                <div class="toast-header">
                    <i class="bi bi-x-circle-fill text-danger me-2"></i>
                    <strong class="me-auto">Campo Requerido</strong>
                    <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
                </div>
                <div class="toast-body">${message}</div>
            </div>`;
        toastContainer.insertAdjacentHTML('beforeend', toastHtml);
        const newToastEl = toastContainer.lastElementChild;
        const newToast = new bootstrap.Toast(newToastEl, { delay: 3000 });
        newToast.show();
        newToastEl.addEventListener('hidden.bs.toast', () => newToastEl.remove());
    }

    if (businessForm) {
        businessForm.addEventListener('submit', function (event) {
            const errors = [];

            // Obtenemos los valores de los campos que queremos validar
            const rfc = document.getElementById('rfc').value.trim();
            const razonSocial = document.getElementById('razonSocial').value.trim();
            const email = document.getElementById('email').value.trim();
            const address = document.getElementById('address').value.trim();
            const phoneNumber = document.getElementById('phoneNumber').value.trim();
            const postalCode = document.getElementById('postalCode').value.trim();

            // Verificamos si están vacíos y añadimos errores
            if (rfc === '') errors.push('El RFC no puede estar vacío.');
            if (razonSocial === '') errors.push('La Razón Social no puede estar vacía.');
            if (email === '') errors.push('El Correo Electrónico no puede estar vacío.');
            if (address === '') errors.push('La Dirección no puede estar vacía.');
            if (phoneNumber === '') errors.push('El Teléfono no puede estar vacío.');
            if (postalCode === '') errors.push('El Código Postal no puede estar vacío.');

            // Si encontramos al menos un error...
            if (errors.length > 0) {
                // ...prevenimos el envío del formulario al backend.
                event.preventDefault();

                // ...y mostramos una notificación por cada error.
                errors.forEach(error => showErrorToast(error));
            }
            // Si no hay errores, el formulario se envía normalmente.
        });
    }
});