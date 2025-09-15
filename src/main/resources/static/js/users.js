document.addEventListener("DOMContentLoaded", function () {
    const usersTableBody = document.getElementById('users-table-body');
    const userModalLabel = document.getElementById('userModalLabel');
    const userForm = document.getElementById('userForm');
    const addUserBtn = document.getElementById('addUserBtn');
    const photoPreview = document.getElementById('photo-preview');

    /**
     * Prepara y llena el modal para editar un usuario.
     * @param {Element} editButton - El botón de editar que fue presionado.
     */
    function populateEditModal(editButton) {
        const userRow = editButton.closest('tr'); // Obtenemos la fila de la tabla

        // Leemos los datos desde los atributos data-* de la fila
        const user = {
            id: userRow.dataset.id,
            firstName: userRow.dataset.firstName,
            lastName: userRow.dataset.lastName,
            email: userRow.dataset.email,
            role: userRow.dataset.role,
            status: userRow.dataset.status,
            photo: userRow.dataset.photo
        };

        userModalLabel.textContent = 'Editar Usuario';
        document.getElementById('userId').value = user.id;
        document.getElementById('userFirstName').value = user.firstName;
        document.getElementById('userLastName').value = user.lastName;
        document.getElementById('userEmail').value = user.email;
        document.getElementById('userRole').value = user.role;
        document.getElementById('userStatus').value = user.status;
        document.getElementById('userPassword').value = '';

        if (user.photo && user.photo !== 'null') {
            photoPreview.src = user.photo;
        } else {
            photoPreview.src = 'https://via.placeholder.com/150';
        }
    }

    function prepareAddModal() {
        userModalLabel.textContent = 'Agregar Nuevo Usuario';
        userForm.reset();
        document.getElementById('userId').value = '';
        photoPreview.src = 'https://via.placeholder.com/150';
    }

    // Listener para los botones de la tabla
    if (usersTableBody) {
        usersTableBody.addEventListener('click', function (event) {
            const editButton = event.target.closest('.edit-user-btn');
            if (editButton) {
                populateEditModal(editButton);
            }
        });
    }

    if (addUserBtn) {
        addUserBtn.addEventListener('click', function () {
            prepareAddModal();
        });
    }
});