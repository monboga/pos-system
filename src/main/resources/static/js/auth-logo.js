document.addEventListener("DOMContentLoaded", function () {
    const LOGO_STORAGE_KEY = 'businessLogo';
    const dynamicLogo = document.getElementById('dynamic-logo');

    if (dynamicLogo) {
        const savedLogoUrl = localStorage.getItem(LOGO_STORAGE_KEY);
        if (savedLogoUrl) {
            // Si hay un logo guardado, lo mostramos
            dynamicLogo.src = savedLogoUrl;
        }
    }
});