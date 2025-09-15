document.addEventListener("DOMContentLoaded", function () {
    const loggedInUser = getLoggedInUser();
    if (!loggedInUser) return;

    // --- SELECTORES DEL DOM ---
    const profileAvatarImg = document.getElementById('profile-avatar-img');
    const profileAvatarInitials = document.getElementById('profile-avatar-initials');
    const profileFullName = document.getElementById('profile-full-name');
    const profileRole = document.getElementById('profile-role');
    const profileFirstName = document.getElementById('profile-first-name');
    const profileLastName = document.getElementById('profile-last-name');
    const profileEmail = document.getElementById('profile-email');
    const profilePhone = document.getElementById('profile-phone');
    const photoUploadInput = document.getElementById('profile-photo-upload');
    const editProfileModalEl = document.getElementById('editProfileModal'); // Selector del modal

    /**
     * Rellena la página con los datos del usuario.
     */
    function renderProfile() {
        const user = getLoggedInUser();

        if (user.photo) {
            if (profileAvatarImg) { profileAvatarImg.src = user.photo; profileAvatarImg.classList.remove('d-none'); }
            if (profileAvatarInitials) profileAvatarInitials.classList.add('d-none');
        } else {
            if (profileAvatarImg) profileAvatarImg.classList.add('d-none');
            if (profileAvatarInitials) { profileAvatarInitials.textContent = getInitials(user.firstName, user.lastName); profileAvatarInitials.classList.remove('d-none'); }
        }

        if (profileFullName) profileFullName.textContent = `${user.firstName} ${user.lastName}`;
        if (profileRole) profileRole.textContent = user.role;
        if (profileFirstName) profileFirstName.textContent = user.firstName;
        if (profileLastName) profileLastName.textContent = user.lastName;
        if (profileEmail) profileEmail.textContent = user.email;
        if (profilePhone) profilePhone.textContent = user.phone;
    }

    /**
     * Lógica para el modal de edición de foto
     */
    if (photoUploadInput) {
        photoUploadInput.addEventListener('change', function (event) {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    const imageUrl = e.target.result;
                    updateUser(loggedInUser.id, { photo: imageUrl });
                    renderProfile();
                    loadSidebarUser();
                };
                reader.readAsDataURL(file);
            }
        });
    }

    // --- LÓGICA AÑADIDA PARA CORREGIR EL BUG ---
    // Evento que se dispara cuando el modal de editar perfil está a punto de mostrarse
    if (editProfileModalEl) {
        editProfileModalEl.addEventListener('show.bs.modal', function () {
            const user = getLoggedInUser();
            const modalAvatarImg = document.getElementById('modal-avatar-img');
            const modalAvatarInitials = document.getElementById('modal-avatar-initials');
            const profileNameModalInput = document.getElementById('profileNameModal');

            // Llenar el nombre
            profileNameModalInput.value = `${user.firstName} ${user.lastName}`;

            // Llenar la imagen o las iniciales
            if (user.photo) {
                modalAvatarImg.src = user.photo;
                modalAvatarImg.classList.remove('d-none');
                modalAvatarInitials.classList.add('d-none');
            } else {
                modalAvatarImg.classList.add('d-none');
                modalAvatarInitials.textContent = getInitials(user.firstName, user.lastName);
                modalAvatarInitials.classList.remove('d-none');
            }
        });
    }
    // --- FIN DE LA CORRECCIÓN ---

    // Renderizado inicial al cargar la página
    renderProfile();
});