document.addEventListener("DOMContentLoaded", function () {
    const usersTableBody = document.getElementById('users-table-body');
    const userModalLabel = document.getElementById('userModalLabel');
    const userForm = document.getElementById('userForm');
    const addUserBtn = document.getElementById('addUserBtn');

    const photoPreviewImg = document.getElementById('photo-preview-img');
    const photoPreviewInitials = document.getElementById('photo-preview-initials');
    const photoUploadInput = document.getElementById('photo-upload');

    const passwordFieldGroup = document.getElementById('password-field-group');
    const passwordStrengthList = document.getElementById('user-password-strength');
    const passwordInput = document.getElementById('userPassword');
    const passwordHelp = document.getElementById('passwordHelp');

    function populateEditModal(editButton) {
        const userRow = editButton.closest('tr');
        const userData = userRow.dataset;

        userModalLabel.textContent = 'Editar Usuario';

        document.getElementById('userId').value = userData.id;
        document.getElementById('userFirstName').value = userData.firstName;
        document.getElementById('userLastName').value = userData.lastName;
        document.getElementById('userEmail').value = userData.email;
        document.getElementById('userPhone').value = userData.phoneNumber;
        document.getElementById('userRole').value = userData.roleId;
        document.getElementById('userStatus').value = userData.status;

        passwordFieldGroup.style.display = 'none';
        passwordInput.required = false;
        passwordHelp.textContent = 'Dejar en blanco para no cambiar la contraseña.';

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

    function prepareAddModal() {
        userModalLabel.textContent = 'Agregar Nuevo Usuario';
        userForm.reset();
        document.getElementById('userId').value = '';

        passwordFieldGroup.style.display = 'block';
        passwordInput.required = true;
        passwordHelp.textContent = '';

        photoPreviewImg.classList.add('d-none');
        photoPreviewInitials.textContent = '';
        photoPreviewInitials.classList.remove('d-none');
    }

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

function getInitials(firstName, lastName) {
    return `${firstName ? firstName[0] : ''}${lastName ? lastName[0] : ''}`.toUpperCase();
}