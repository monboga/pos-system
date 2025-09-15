const USER_DATA_KEY = 'posUsersData';
const LOGGED_IN_USER_ID_KEY = 'loggedInUserId'; // Usaremos sessionStorage

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
 */
function getUsers() {
    return JSON.parse(localStorage.getItem(USER_DATA_KEY)) || [];
}

/**
 * Obtiene un usuario por su ID.
 */
function getUserById(userId) {
    const users = getUsers();
    return users.find(user => user.id === parseInt(userId)) || null;
}

/**
 * (NUEVO) Busca un usuario por su correo electrónico.
 * @param {string} email - El correo a buscar.
 * @returns {object|null} El objeto de usuario o null.
 */
function findUserByEmail(email) {
    const users = getUsers();
    return users.find(user => user.email.toLowerCase() === email.toLowerCase()) || null;
}


/**
 * Actualiza los datos de un usuario.
 */
function updateUser(userId, updatedData) {
    let users = getUsers();
    users = users.map(user => (user.id === parseInt(userId)) ? { ...user, ...updatedData } : user);
    localStorage.setItem(USER_DATA_KEY, JSON.stringify(users));
}

/**
 * (MODIFICADO) Obtiene el usuario que ha iniciado sesión leyendo desde sessionStorage.
 */
function getLoggedInUser() {
    const loggedInUserId = sessionStorage.getItem(LOGGED_IN_USER_ID_KEY);
    if (!loggedInUserId) {
        return null; // Nadie ha iniciado sesión
    }
    return getUserById(loggedInUserId);
}

/**
 * Genera las iniciales a partir de un nombre y apellido.
 */
function getInitials(firstName, lastName) {
    return `${firstName ? firstName[0] : ''}${lastName ? lastName[0] : ''}`.toUpperCase();
}

initializeUsers();