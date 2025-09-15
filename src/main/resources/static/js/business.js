document.addEventListener("DOMContentLoaded", function () {
    const logoUploadInput = document.getElementById('logoUpload');
    const logoPreview = document.getElementById('business-logo-preview'); // Vista previa en la página
    const LOGO_STORAGE_KEY = 'businessLogo';

    // Cargar el logo guardado en la vista previa al cargar la página
    const savedLogoUrl = localStorage.getItem(LOGO_STORAGE_KEY);
    if (savedLogoUrl && logoPreview) {
        logoPreview.src = savedLogoUrl;
    }

    if (logoUploadInput && logoPreview) {
        logoUploadInput.addEventListener('change', function (event) {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();

                reader.onload = function (e) {
                    const imageUrl = e.target.result;
                    // Guardamos la imagen en formato Base64 en localStorage
                    localStorage.setItem(LOGO_STORAGE_KEY, imageUrl);

                    // Actualizamos AMBAS imágenes: la de la vista previa y la del sidebar.
                    logoPreview.src = imageUrl;

                    const sidebarLogo = document.getElementById('sidebar-logo');
                    if (sidebarLogo) {
                        sidebarLogo.src = imageUrl;
                        // También nos aseguramos de que la imagen sea visible y el ícono se oculte
                        sidebarLogo.classList.remove('d-none');
                        document.getElementById('sidebar-logo-icon').classList.add('d-none');
                    }
                };

                reader.readAsDataURL(file);
            }
        });
    }
});