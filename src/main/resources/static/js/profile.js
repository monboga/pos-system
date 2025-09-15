document.addEventListener("DOMContentLoaded", function () {
    // Selectores de los modales
    const editProfileModalEl = document.getElementById('editProfileModal');
    const editInfoModalEl = document.getElementById('editInfoModal');

    // --- LÓGICA PARA POBLAR MODALES ---

    // Rellenar el modal de "Editar Perfil"
    if (editProfileModalEl) {
        editProfileModalEl.addEventListener('show.bs.modal', function () {
            // Leemos los datos directamente del HTML renderizado por Thymeleaf
            const fullName = document.getElementById('profile-full-name').textContent;
            const photoSrc = document.getElementById('profile-avatar-img')?.src;
            const initials = document.getElementById('profile-avatar-initials')?.textContent;

            document.getElementById('profileFullNameModal').value = fullName;

            const modalAvatarImg = document.getElementById('modal-avatar-img');
            const modalAvatarInitials = document.getElementById('modal-avatar-initials');

            if (photoSrc && !photoSrc.endsWith('null')) { // Thymeleaf puede imprimir 'null' como string
                modalAvatarImg.src = photoSrc;
                modalAvatarImg.classList.remove('d-none');
                modalAvatarInitials.classList.add('d-none');
            } else {
                modalAvatarImg.classList.add('d-none');
                modalAvatarInitials.textContent = initials;
                modalAvatarInitials.classList.remove('d-none');
            }
        });
    }

    // Rellenar el modal de "Editar Información Personal"
    if (editInfoModalEl) {
        editInfoModalEl.addEventListener('show.bs.modal', function () {
            // Leemos los datos directamente del HTML
            document.getElementById('infoFirstName').value = document.getElementById('profile-first-name').textContent;
            document.getElementById('infoLastName').value = document.getElementById('profile-last-name').textContent;
            document.getElementById('infoEmail').value = document.getElementById('profile-email').textContent;
            document.getElementById('infoPhone').value = document.getElementById('profile-phone').textContent;
        });
    }
});