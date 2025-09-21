document.addEventListener("DOMContentLoaded", function () {
    // --- ESTADO DE LA APLICACIÓN ---
    let cart = {};
    let activeCategories = [];
    let searchTerm = '';

    // --- SELECTORES DEL DOM ---
    const productGrid = document.getElementById('product-grid');
    const invoiceItemsContainer = document.getElementById('invoice-items');
    const subtotalEl = document.getElementById('subtotal');
    const ivaEl = document.getElementById('iva');
    const totalEl = document.getElementById('total');
    const clientSelect = document.getElementById('client-select');
    const confirmSaleBtn = document.getElementById('confirm-sale-btn');
    const confirmSaleModalEl = document.getElementById('confirmSaleModal');
    const confirmSaleModal = confirmSaleModalEl ? new bootstrap.Modal(confirmSaleModalEl) : null;
    const saveAsTicketBtn = document.getElementById('saveAsTicketBtn');
    const saleSuccessModalEl = document.getElementById('saleSuccessModal');
    const saleSuccessModal = saleSuccessModalEl ? new bootstrap.Modal(saleSuccessModalEl) : null;
    const saleSuccessMessage = document.getElementById('saleSuccessMessage');
    const categoryCardsContainer = document.getElementById('category-cards-container');
    const clearCategoryFiltersBtn = document.getElementById('clear-category-filters');
    const productSearchInput = document.getElementById('product-search-input'); // Este es el que causaba el error
    const clientNameDisplay = document.getElementById('client-name-display');
    const clientRfcDisplay = document.getElementById('client-rfc-display');
    const clientEmailDisplay = document.getElementById('client-email-display');

    const csrfMeta = document.querySelector('meta[name="_csrf"]');
    const csrfHeaderMeta = document.querySelector('meta[name="_csrf_header"]');
    const csrfToken = csrfMeta ? csrfMeta.getAttribute('content') : null;
    const csrfHeader = csrfHeaderMeta ? csrfHeaderMeta.getAttribute('content') : null;

    if (clientSelect) {
        // 1. Inicializamos Select2
        $(clientSelect).select2({
            theme: "bootstrap-5",
            placeholder: "Seleccionar cliente...",
            allowClear: true
        });

        // 2. Usamos el evento 'change' de jQuery, que funciona mejor con Select2
        $(clientSelect).on('change', function () {
            const selectedOption = $(this).find('option:selected');
            const name = selectedOption.data('nombre');
            const rfc = selectedOption.data('rfc');
            const correo = selectedOption.data('correo');

            if ($(this).val()) {
                // Si se selecciona un cliente, poblamos los campos
                clientNameDisplay.value = name || '';
                clientRfcDisplay.value = rfc || '';
                clientEmailDisplay.value = correo || '';
            } else {
                // Si se limpia la selección, vaciamos los campos
                clientNameDisplay.value = '';
                clientRfcDisplay.value = '';
                clientEmailDisplay.value = '';
            }
        });
    }

    if (confirmSaleBtn) {
        confirmSaleBtn.addEventListener('click', function () {
            const validationErrors = [];
            if (!clientSelect.value) { validationErrors.push('Falta seleccionar cliente'); }
            if (Object.keys(cart).length === 0) { validationErrors.push('Falta seleccionar al menos un producto'); }
            if (validationErrors.length > 0) {
                validationErrors.forEach(error => showErrorToast(error));
                return;
            }
            if (confirmSaleModal) confirmSaleModal.show();
        });
    }

    // Listener para el botón "No, guardar como Ticket"
    if (saveAsTicketBtn) {
        saveAsTicketBtn.addEventListener('click', function () {

            // 1. Deshabilitamos el botón para prevenir dobles clics
            saveAsTicketBtn.disabled = true;
            saveAsTicketBtn.innerHTML = `
                <span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                Guardando...
            `;

            if (!csrfToken || !csrfHeader) {
                showErrorToast('Error de seguridad. No se pudo procesar la venta.');
                // Volvemos a habilitar el botón si hay un error temprano
                saveAsTicketBtn.disabled = false;
                saveAsTicketBtn.textContent = 'No';
                return;
            }
            if (confirmSaleModal) confirmSaleModal.hide();

            // 2. Construimos el objeto de datos (DTO) para enviar al backend
            const saleData = {
                clientRfc: clientSelect.value,
                items: Object.keys(cart).map(productId => ({
                    id: parseInt(productId), // Aseguramos que el ID sea un número
                    quantity: cart[productId].quantity
                }))
            };

            // 3. Usamos la API Fetch para enviar los datos como JSON
            fetch('/pos/create-sale', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    [csrfHeader]: csrfToken // Incluimos el token CSRF para la seguridad
                },
                body: JSON.stringify(saleData)
            })
                .then(response => response.json().then(data => ({ ok: response.ok, data })))
                .then(({ ok, data }) => {
                    if (!ok) {
                        // Si el backend devuelve un error (ej. sin stock), lo mostramos
                        throw new Error(data.message || 'Ocurrió un error desconocido.');
                    }

                    // 4. Si todo fue exitoso:
                    // a. Mostramos el modal de éxito con el número de venta
                    saleSuccessMessage.textContent = `${data.message} Número de venta: ${data.saleNumber}`;
                    saleSuccessModal.show();

                    // b. Limpiamos el carrito y refrescamos la vista del pedido
                    cart = {};
                    renderCart();
                })
                .catch(error => {
                    // 5. Si algo falló, mostramos un toast de error
                    showErrorToast(error.message);
                }).finally(() => {
                    // 2. Volvemos a habilitar el botón cuando la operación termina (éxito o error)
                    saveAsTicketBtn.disabled = false;
                    saveAsTicketBtn.textContent = 'No';
                });
        });
    }

    // --- LÓGICA DE NOTIFICACIONES TOAST ---
    function showErrorToast(message) {
        const toastContainer = document.querySelector('.toast-container');
        if (!toastContainer) return;
        const toastHtml = `<div class="toast" role="alert" aria-live="assertive" aria-atomic="true"><div class="toast-header"><i class="bi bi-x-circle-fill text-danger me-2"></i><strong class="me-auto">Error de Validación</strong><button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button></div><div class="toast-body">${message}</div></div>`;
        toastContainer.insertAdjacentHTML('beforeend', toastHtml);
        const newToastEl = toastContainer.lastElementChild;
        const newToast = new bootstrap.Toast(newToastEl, { delay: 3000 });
        newToast.show();
        newToastEl.addEventListener('hidden.bs.toast', () => newToastEl.remove());
    }

    // --- LÓGICA DEL CARRITO ---
    function renderCart() {
        invoiceItemsContainer.innerHTML = '';
        if (Object.keys(cart).length === 0) {
            invoiceItemsContainer.innerHTML = '<p class="text-muted text-center mt-4">No hay productos en el pedido.</p>';
            calculateTotals();
            updateCatalogView(); // Asegura que el catálogo se resetee visualmente
            return;
        }

        for (const productId in cart) {
            const item = cart[productId];
            // Lógica para decidir si mostrar la imagen o el placeholder
            let imageHtml;
            // Agregamos una variable para controlar si el botón debe estar deshabilitado
            const isIncreaseDisabled = item.quantity >= item.stock;
            if (item.image && item.image !== 'null' && item.image.trim() !== '') {
                // Si hay una imagen válida, creamos la etiqueta <img>
                imageHtml = `<img src="${item.image}" alt="${item.name}" style="width: 50px; height: 50px; object-fit: cover; border-radius: 0.5rem;">`;
            } else {
                // Si no, creamos nuestro div de placeholder
                imageHtml = `
                    <div style="width: 50px; height: 50px; flex-shrink: 0;">
                        <div class="image-placeholder h-100">
                            <i class="bi bi-image-fill"></i>
                        </div>
                    </div>
                `;
            }

            const itemHtml = `
                <div class="invoice-item mb-3">
                    ${imageHtml}
                    <div class="item-details">
                        <div class="fw-bold">${item.name}</div>
                        <small class="text-muted">${item.description}</small>
                    </div>
                    <div class="quantity-controls-vertical">
                        <button data-id="${productId}" data-action="increase" ${isIncreaseDisabled ? 'disabled' : ''}>+</button>
                        <span class="quantity-value">${item.quantity}</span>
                        <button data-id="${productId}" data-action="decrease">-</button>
                    </div>
                    <div class="fw-bold ms-3" style="width: 80px; text-align: right;">$${(item.price * item.quantity).toFixed(2)}</div>
                </div>
            `;
            invoiceItemsContainer.insertAdjacentHTML('beforeend', itemHtml);
        }
        calculateTotals();
        updateCatalogView();
    }

    // ... (El resto de las funciones: calculateTotals, handleCartAction, updateCatalogView, applyFilters, etc., se mantienen exactamente igual)
    function calculateTotals() { const subtotal = Object.values(cart).reduce((sum, item) => sum + (item.price * item.quantity), 0); const iva = subtotal * 0.16; const total = subtotal + iva; subtotalEl.textContent = `$${subtotal.toFixed(2)}`; ivaEl.textContent = `$${iva.toFixed(2)}`; totalEl.textContent = `$${total.toFixed(2)}`; }
    function handleCartAction(productId, action, productData = {}) {
        const existingItem = cart[productId];
        if (action === 'increase') {
            if (existingItem) {
                if (existingItem.quantity < existingItem.stock) {
                    cart[productId].quantity++;
                }
            } else {
                if (productData.stock > 0) {
                    cart[productId] = { ...productData, quantity: 1 };
                }
            }
        }
        else if (action === 'decrease') {
            if (existingItem) {
                cart[productId].quantity--;
                if (cart[productId].quantity <= 0) {
                    delete cart[productId];
                }
            }
        }
        renderCart();
    }
    function updateCatalogView() {
        document.querySelectorAll('.product-container').forEach(cardContainer => {
            const productId = cardContainer.dataset.id;
            const stock = parseInt(cardContainer.dataset.stock, 10);
            const quantityValueEl = cardContainer.querySelector('.quantity-value');
            const increaseBtn = cardContainer.querySelector('.btn-increase');

            if (cart[productId]) {
                cardContainer.querySelector('.product-card').classList.add('in-cart');
                quantityValueEl.textContent = cart[productId].quantity;
                // Deshabilita el botón '+' si la cantidad en el carrito iguala o supera el stock
                if (increaseBtn) {
                    increaseBtn.disabled = cart[productId].quantity >= stock;
                }
            } else {
                cardContainer.querySelector('.product-card').classList.remove('in-cart');
                if (quantityValueEl) quantityValueEl.textContent = '0';
                // Habilita el botón si el producto ya no está en el carrito
                if (increaseBtn) {
                    increaseBtn.disabled = false;
                }
            }
        });
    }
    function applyFilters() { const allProducts = productGrid.querySelectorAll('.product-container'); allProducts.forEach(product => { const productName = product.dataset.name.toLowerCase(); const productCategory = product.dataset.categoryName; const nameMatch = productName.includes(searchTerm); const categoryMatch = activeCategories.length === 0 || activeCategories.includes(productCategory); if (nameMatch && categoryMatch) { product.style.display = 'block'; } else { product.style.display = 'none'; } }); }
    function updateCategoryCardsVisualState() { const allCategoryCards = categoryCardsContainer.querySelectorAll('.category-card'); allCategoryCards.forEach(card => { const cardCategoryName = card.dataset.categoryName; if (cardCategoryName === 'all') { card.classList.toggle('active', activeCategories.length === 0); } else { card.classList.toggle('active', activeCategories.includes(cardCategoryName)); } }); }

    // --- EVENT LISTENERS (sin cambios) ---
    if (productSearchInput) {
        productSearchInput.addEventListener('input', function (e) { searchTerm = e.target.value.toLowerCase(); applyFilters(); });
    }

    if (categoryCardsContainer) {
        categoryCardsContainer.addEventListener('click', function (e) { const clickedCard = e.target.closest('.category-card'); if (!clickedCard) return; e.preventDefault(); const categoryName = clickedCard.dataset.categoryName; if (categoryName === 'all') { activeCategories = []; } else { const index = activeCategories.indexOf(categoryName); if (index > -1) { activeCategories.splice(index, 1); } else { activeCategories.push(categoryName); } } updateCategoryCardsVisualState(); applyFilters(); });
    }

    if (clearCategoryFiltersBtn) {
        clearCategoryFiltersBtn.addEventListener('click', function () { activeCategories = []; updateCategoryCardsVisualState(); applyFilters(); });
    }

    if (productGrid) {
        productGrid.addEventListener('click', function (e) {
            const button = e.target.closest('.quantity-controls button');
            if (!button) return;
            const action = button.dataset.action;
            const productContainer = button.closest('.product-container');
            const productId = productContainer.dataset.id;
            const productData = {
                id: productId,
                name: productContainer.dataset.name,
                description: productContainer.dataset.description,
                price: parseFloat(productContainer.dataset.price),
                image: productContainer.dataset.image,
                stock: parseInt(productContainer.dataset.stock, 10)
            };
            handleCartAction(productId, action, productData);
        });
    }

    if (invoiceItemsContainer) {
        invoiceItemsContainer.addEventListener('click', function (e) { const button = e.target.closest('button[data-action]'); if (!button) return; const productId = button.dataset.id; const action = button.dataset.action; handleCartAction(productId, action); });
    }

    renderCart();
});