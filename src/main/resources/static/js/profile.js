document.addEventListener("DOMContentLoaded", function () {
    const loggedInUser = getLoggedInUser();
    if (!loggedInUser) return;

    // --- SELECTORES DEL DOM ---
    // Avatares
    const profileAvatarImg = document.getElementById('profile-avatar-img');
    const profileAvatarInitials = document.getElementById('profile-avatar-initials');

    // Campos de texto en la vista principal
    const profileFullName = document.getElementById('profile-full-name');
    const profileRole = document.getElementById('profile-role');
    const profileFirstName = document.getElementById('profile-first-name');
    const profileLastName = document.getElementById('profile-last-name');
    const profileEmail = document.getElementById('profile-email');
    const profilePhone = document.getElementById('profile-phone');

    // Campos del modal de edición
    const photoUploadInput = document.getElementById('profile-photo-upload');

    /**
     * Rellena la página con los datos del usuario.
     */
    function renderProfile() {
        const user = getLoggedInUser(); // Obtenemos los datos más frescos

        // Lógica de avatar/foto
        if (user.photo) {
            if (profileAvatarImg) {
                profileAvatarImg.src = user.photo;
                profileAvatarImg.classList.remove('d-none');
            }
            if (profileAvatarInitials) profileAvatarInitials.classList.add('d-none');
        } else {
            if (profileAvatarImg) profileAvatarImg.classList.add('d-none');
            if (profileAvatarInitials) {
                profileAvatarInitials.textContent = getInitials(user.firstName, user.lastName);
                profileAvatarInitials.classList.remove('d-none');
            }
        }

        // --- LÓGICA AÑADIDA PARA CORREGIR EL BUG ---
        // Rellenamos los campos de texto con los datos del usuario.
        if (profileFullName) profileFullName.textContent = `${user.firstName} ${user.lastName}`;
        if (profileRole) profileRole.textContent = user.role;
        if (profileFirstName) profileFirstName.textContent = user.firstName;
        if (profileLastName) profileLastName.textContent = user.lastName;
        if (profileEmail) profileEmail.textContent = user.email;
        if (profilePhone) profilePhone.textContent = user.phone;
        // --- FIN DE LA CORRECCIÓN ---
    }

    // Lógica para el modal de edición de foto
    if (photoUploadInput) {
        photoUploadInput.addEventListener('change', function (event) {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    const imageUrl = e.target.result;
                    // Actualizamos el usuario en nuestra "DB" simulada
                    updateUser(loggedInUser.id, { photo: imageUrl });
                    // Re-renderizamos el perfil y el sidebar para reflejar el cambio al instante
                    renderProfile();
                    loadSidebarUser();
                };
                reader.readAsDataURL(file);
            }
        });
    }

    // Renderizado inicial al cargar la página
    renderProfile();
});