document.addEventListener("DOMContentLoaded", function () {
    // --- SELECTORES DEL DOM ---
    const userModalEl = document.getElementById('userModal');
    if (!userModalEl) return; // Si no hay modal, no hacemos nada.

    const userModalLabel = document.getElementById('userModalLabel');
    const userForm = document.getElementById('userForm');

    const photoPreviewImg = document.getElementById('photo-preview-img');
    const photoPreviewInitials = document.getElementById('photo-preview-initials');
    const photoUploadInput = document.getElementById('photo-upload');
    const firstNameInput = document.getElementById('userFirstName');
    const lastNameInput = document.getElementById('userLastName');
    const passwordFieldGroup = document.getElementById('password-field-group');
    const passwordInput = document.getElementById('userPassword');
    const passwordHelp = document.getElementById('passwordHelp');

    /**
     * Prepara y llena el modal para el modo EDICIÓN.
     * @param {Element} button - El botón de editar que disparó el evento.
     */
    function setupEditModal(button) {
        const userRow = button.closest('tr');
        const userData = userRow.dataset;

        userModalLabel.textContent = 'Editar Usuario';

        // Llenar campos de texto del formulario
        document.getElementById('userId').value = userData.id;
        firstNameInput.value = userData.firstName;
        lastNameInput.value = userData.lastName;
        document.getElementById('userEmail').value = userData.email;
        document.getElementById('userPhone').value = userData.phoneNumber;
        document.getElementById('userRole').value = userData.roleId;
        document.getElementById('userStatus').value = userData.status;

        // Ocultar campo de contraseña
        passwordFieldGroup.style.display = 'none';
        passwordInput.required = false;

        // Lógica para mostrar foto o iniciales
        if (userData.photo && userData.photo !== 'null') {
            photoPreviewImg.src = userData.photo;
            photoPreviewImg.classList.remove('d-none');
            photoPreviewInitials.classList.add('d-none');
        } else {
            photoPreviewImg.classList.add('d-none');
            photoPreviewInitials.textContent = getInitials(userData.firstName, userData.lastName);
            photoPreviewInitials.classList.remove('d-none');
        }
    }

    /**
     * Prepara el modal para el modo AGREGAR.
     */
    function setupAddModal() {
        userModalLabel.textContent = 'Agregar Nuevo Usuario';
        userForm.reset(); // Limpia el formulario
        document.getElementById('userId').value = '';
        passwordFieldGroup.style.display = 'block';
        passwordInput.required = true;
        updateInitialsPreview(); // Llama para mostrar iniciales vacías o basadas en texto residual
        const userRoleSelect = document.getElementById('userRole');
        if (userRoleSelect) {
            // Itera sobre las opciones del select
            for (let i = 0; i < userRoleSelect.options.length; i++) {
                const option = userRoleSelect.options[i];
                // Si el texto de la opción es 'USER', la selecciona
                if (option.text.toUpperCase() === 'USER') {
                    userRoleSelect.value = option.value;
                    break; // Termina el bucle una vez encontrado
                }
            }
        }
    }

    /**
     * Limpia y resetea completamente el modal a su estado original.
     */
    function resetModal() {
        userForm.reset();
        document.getElementById('userId').value = '';
        photoPreviewImg.src = '';
        photoPreviewImg.classList.add('d-none');
        photoPreviewInitials.textContent = '';
        photoPreviewInitials.classList.remove('d-none');
    }

    /**
     * Actualiza las iniciales en el avatar de previsualización en vivo.
     */
    function updateInitialsPreview() {
        // Solo actualiza las iniciales si no se ha subido una foto
        if (photoUploadInput.files.length === 0) {
            const initials = getInitials(firstNameInput.value, lastNameInput.value);
            photoPreviewInitials.textContent = initials;
            photoPreviewInitials.classList.remove('d-none');
            photoPreviewImg.classList.add('d-none');
        }
    }

    // --- EVENTOS DEL CICLO DE VIDA DEL MODAL ---

    // Evento que se dispara ANTES de que el modal se muestre
    userModalEl.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget; // El botón que disparó el modal
        // Verificamos si el botón es de 'editar'
        if (button && button.classList.contains('edit-user-btn')) {
            setupEditModal(button);
        } else {
            // Si no, es el botón de 'agregar'
            setupAddModal();
        }
    });

    // Evento que se dispara DESPUÉS de que el modal se oculte
    userModalEl.addEventListener('hidden.bs.modal', function () {
        resetModal();
    });

    // --- OTROS EVENT LISTENERS ---

    // Listener para la previsualización de la imagen
    if (photoUploadInput) {
        photoUploadInput.addEventListener('change', function (event) {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    photoPreviewImg.src = e.target.result;
                    photoPreviewImg.classList.remove('d-none');
                    photoPreviewInitials.classList.add('d-none');
                };
                reader.readAsDataURL(file);
            }
        });
    }

    // Listeners para iniciales en vivo
    if (firstNameInput) firstNameInput.addEventListener('input', updateInitialsPreview);
    if (lastNameInput) lastNameInput.addEventListener('input', updateInitialsPreview);
});

function getInitials(firstName, lastName) {
    return `${firstName ? firstName[0] : ''}${lastName ? lastName[0] : ''}`.toUpperCase();
}