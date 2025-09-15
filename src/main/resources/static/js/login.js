document.addEventListener("DOMContentLoaded", function () {
    const loginForm = document.getElementById('loginForm');
    const emailInput = document.getElementById('loginEmail');
    const errorAlert = document.getElementById('errorAlert');

    if (loginForm) {
        loginForm.addEventListener('submit', function (event) {
            event.preventDefault(); // Evitamos que el formulario se envíe de la forma tradicional

            const email = emailInput.value;
            // Para esta simulación, ignoraremos la contraseña.

            const user = findUserByEmail(email);

            if (user) {
                // Éxito: Guardamos el ID del usuario en sessionStorage
                sessionStorage.setItem('loggedInUserId', user.id);
                // Redirigimos al dashboard
                window.location.href = '/dashboard';
            } else {
                // Error: Mostramos una alerta
                errorAlert.textContent = 'Correo electrónico o contraseña incorrectos.';
                errorAlert.classList.remove('d-none');
            }
        });
    }
});