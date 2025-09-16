document.addEventListener("DOMContentLoaded", function () {
    const logoUploadInput = document.getElementById('logoUpload');
    const logoPreview = document.getElementById('business-logo-preview');

    if (logoUploadInput && logoPreview) {
        logoUploadInput.addEventListener('change', function (event) {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();

                // Cuando el lector termine, pone el resultado como src de la imagen de vista previa
                reader.onload = function (e) {
                    logoPreview.src = e.target.result;
                };

                // Lee el archivo para activar el evento 'onload'
                reader.readAsDataURL(file);
            }
        });
    }
});