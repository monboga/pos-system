// Clave para guardar los datos en localStorage
const USER_DATA_KEY = 'posUsersData';

/**
 * Inicializa los datos de usuario con valores por defecto si no existen.
 */
function initializeUsers() {
    if (!localStorage.getItem(USER_DATA_KEY)) {
        const defaultUsers = [
            { id: 1, firstName: 'Admin', lastName: 'Usuario', email: 'admin@pos.com', phone: '+52 8100000000', role: 'Administrador', status: 1, photo: null },
            { id: 2, firstName: 'Juan', lastName: 'Pérez', email: 'juan.perez@example.com', phone: '+52 8100001111', role: 'Vendedor', status: 1, photo: 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?q=80&w=200&auto=format&fit=crop' },
            { id: 3, firstName: 'Ana', lastName: 'Gómez', email: 'ana.gomez@example.com', phone: '+52 8100002222', role: 'Vendedor', status: 0, photo: null }
        ];
        localStorage.setItem(USER_DATA_KEY, JSON.stringify(defaultUsers));
    }
}

/**
 * Obtiene todos los usuarios.
 * @returns {Array} Array de objetos de usuario.
 */
function getUsers() {
    return JSON.parse(localStorage.getItem(USER_DATA_KEY)) || [];
}

/**
 * Obtiene un usuario por su ID.
 * @param {number} userId - El ID del usuario.
 * @returns {object|null} El objeto de usuario o null si no se encuentra.
 */
function getUserById(userId) {
    const users = getUsers();
    return users.find(user => user.id === parseInt(userId)) || null;
}

/**
 * Actualiza los datos de un usuario.
 * @param {number} userId - El ID del usuario a actualizar.
 * @param {object} updatedData - Un objeto con los campos a actualizar.
 */
function updateUser(userId, updatedData) {
    let users = getUsers();
    users = users.map(user => {
        if (user.id === parseInt(userId)) {
            return { ...user, ...updatedData };
        }
        return user;
    });
    localStorage.setItem(USER_DATA_KEY, JSON.stringify(users));
}

/**
 * Simula obtener el usuario que ha iniciado sesión (para este proyecto, siempre el ID 1).
 * @returns {object|null} El objeto del usuario logueado.
 */
function getLoggedInUser() {
    return getUserById(1);
}

/**
 * Genera las iniciales a partir de un nombre y apellido.
 * @param {string} firstName - El primer nombre.
 * @param {string} lastName - El apellido.
 * @returns {string} Las iniciales (ej. "AU").
 */
function getInitials(firstName, lastName) {
    return `${firstName ? firstName[0] : ''}${lastName ? lastName[0] : ''}`.toUpperCase();
}


// Inicializamos los datos al cargar el script por primera vez
initializeUsers();