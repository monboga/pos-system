document.addEventListener("DOMContentLoaded", function () {
    // --- SELECTORES DEL DOM ---
    const usersTableBody = document.getElementById('users-table-body');
    const userModal = new bootstrap.Modal(document.getElementById('userModal'));
    const userModalLabel = document.getElementById('userModalLabel');
    const userForm = document.getElementById('userForm');
    const addUserBtn = document.getElementById('addUserBtn');
    const photoPreview = document.getElementById('photo-preview'); // Selector para la vista previa

    /**
     * Renderiza la tabla de usuarios con los datos de localStorage.
     */
    function renderUserTable() {
        if (!usersTableBody) return;

        const users = getUsers();
        usersTableBody.innerHTML = '';

        users.forEach(user => {
            const photoHtml = user.photo
                ? `<img src="${user.photo}" class="user-photo" alt="Foto de ${user.firstName}">`
                : `<div class="user-avatar-initials">${getInitials(user.firstName, user.lastName)}</div>`;

            const statusHtml = user.status === 1
                ? `<span class="badge rounded-pill status-active">Activo</span>`
                : `<span class="badge rounded-pill status-inactive">Inactivo</span>`;

            const row = `
                <tr>
                    <td>${photoHtml}</td>
                    <td>${user.firstName} ${user.lastName}</td>
                    <td>${user.email}</td>
                    <td><span class="badge bg-primary">${user.role}</span></td>
                    <td>${statusHtml}</td>
                    <td>2025-09-15</td>
                    <td>
                        <button class="btn btn-sm btn-outline-secondary edit-user-btn" data-user-id="${user.id}" data-bs-toggle="modal" data-bs-target="#userModal">
                            <i class="bi bi-pencil"></i>
                        </button>
                        <button class="btn btn-sm btn-outline-danger delete-user-btn" data-user-id="${user.id}">
                            <i class="bi bi-trash"></i>
                        </button>
                    </td>
                </tr>
            `;
            usersTableBody.insertAdjacentHTML('beforeend', row);
        });
    }

    /**
     * Prepara y llena el modal para editar un usuario.
     * @param {number} userId - El ID del usuario a editar.
     */
    function populateEditModal(userId) {
        const user = getUserById(userId);
        if (!user) return;

        userModalLabel.textContent = 'Editar Usuario';
        document.getElementById('userId').value = user.id;
        document.getElementById('userFirstName').value = user.firstName;
        document.getElementById('userLastName').value = user.lastName;
        document.getElementById('userEmail').value = user.email;
        document.getElementById('userRole').value = user.role;
        document.getElementById('userStatus').value = user.status;
        document.getElementById('userPassword').value = '';

        // --- LÍNEAS AÑADIDAS PARA CORREGIR EL BUG ---
        if (user.photo) {
            photoPreview.src = user.photo;
        } else {
            // Si no hay foto, muestra la imagen por defecto
            photoPreview.src = 'https://via.placeholder.com/150';
        }
        // --- FIN DE LA CORRECCIÓN ---
    }

    /**
     * Prepara el modal para agregar un nuevo usuario (limpia el formulario).
     */
    function prepareAddModal() {
        userModalLabel.textContent = 'Agregar Nuevo Usuario';
        userForm.reset();
        document.getElementById('userId').value = '';
        photoPreview.src = 'https://via.placeholder.com/150'; // Restaura la imagen por defecto
    }


    // --- EVENT LISTENERS ---

    if (usersTableBody) {
        usersTableBody.addEventListener('click', function (event) {
            const editButton = event.target.closest('.edit-user-btn');
            if (editButton) {
                const userId = editButton.dataset.userId;
                populateEditModal(userId);
            }
        });
    }

    if (addUserBtn) {
        addUserBtn.addEventListener('click', function () {
            prepareAddModal();
        });
    }

    renderUserTable();
});