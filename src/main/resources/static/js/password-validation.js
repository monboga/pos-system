/**
 * Inicializa la funcionalidad de mostrar/ocultar y la validación de fortaleza
 * para un campo de contraseña específico.
 * @param {string} passwordInputId - El ID del input de la contraseña.
 * @param {string} toggleIconId - El ID del ícono del ojo.
 * @param {string} strengthListId - El ID de la lista <ul> de requisitos.
 */
function initializePasswordValidation(passwordInputId, toggleIconId, strengthListId) {
    const passwordInput = document.getElementById(passwordInputId);
    const toggleIcon = document.getElementById(toggleIconId);
    const strengthList = document.getElementById(strengthListId);

    // Salimos si alguno de los elementos principales no existe.
    if (!passwordInput || !toggleIcon) {
        return;
    }

    // --- Lógica para mostrar/ocultar contraseña ---
    toggleIcon.addEventListener('click', function () {
        // Cambia el tipo de input entre 'password' y 'text'
        const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
        passwordInput.setAttribute('type', type);

        // Cambia el ícono del ojo entre abierto y cerrado
        this.classList.toggle('bi-eye-fill');
        this.classList.toggle('bi-eye-slash-fill');
    });

    // --- Lógica para la validación de fortaleza en tiempo real ---
    // Solo se ejecuta si se proporcionó una lista de requisitos
    if (strengthList) {
        const validations = {
            length: strengthList.querySelector('[data-validate="length"]'),
            uppercase: strengthList.querySelector('[data-validate="uppercase"]'),
            number: strengthList.querySelector('[data-validate="number"]'),
            special: strengthList.querySelector('[data-validate="special"]')
        };

        passwordInput.addEventListener('input', function () {
            const value = this.value;

            // 1. Validar longitud (mínimo 12 caracteres)
            if (validations.length) {
                validations.length.classList.toggle('valid', value.length >= 12);
            }
            // 2. Validar letra mayúscula
            if (validations.uppercase) {
                validations.uppercase.classList.toggle('valid', /[A-Z]/.test(value));
            }
            // 3. Validar número
            if (validations.number) {
                validations.number.classList.toggle('valid', /[0-9]/.test(value));
            }
            // 4. Validar caracter especial
            if (validations.special) {
                validations.special.classList.toggle('valid', /[^A-Za-z0-9]/.test(value));
            }
        });
    }
}