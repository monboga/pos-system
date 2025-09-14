document.addEventListener("DOMContentLoaded", function () {
    let cart = {};

    const productGrid = document.getElementById('product-grid');
    const invoiceItemsContainer = document.getElementById('invoice-items');
    const subtotalEl = document.getElementById('subtotal');
    const ivaEl = document.getElementById('iva');
    const totalEl = document.getElementById('total');

    /**
     * Función principal que actualiza TODAS las vistas (Factura y Catálogo)
     */
    function renderViews() {
        renderInvoice();
        updateCatalogView();
    }

    /**
     * Renderiza la lista de items en la factura.
     */
    function renderInvoice() {
        invoiceItemsContainer.innerHTML = '';
        if (Object.keys(cart).length === 0) {
            invoiceItemsContainer.innerHTML = '<p class="text-muted text-center mt-4">No hay productos en el pedido.</p>';
        }

        for (const productId in cart) {
            const item = cart[productId];
            const itemHtml = `
                <div class="invoice-item mb-3">
                    <img src="${item.image}" alt="${item.name}">
                    <div class="item-details">
                        <div class="fw-bold">${item.name}</div>
                        <small class="text-muted">${item.description}</small>
                    </div>
                    <div class="quantity-controls-vertical">
                        <button data-id="${productId}" data-action="increase">+</button>
                        <span class="quantity-value">${item.quantity}</span>
                        <button data-id="${productId}" data-action="decrease">-</button>
                    </div>
                    <div class="fw-bold ms-3" style="width: 80px; text-align: right;">$${(item.price * item.quantity).toFixed(2)}</div>
                </div>
            `;
            invoiceItemsContainer.insertAdjacentHTML('beforeend', itemHtml);
        }
        calculateTotals();
    }

    /**
     * Actualiza las tarjetas del catálogo para reflejar las cantidades del carrito.
     */
    function updateCatalogView() {
        document.querySelectorAll('.product-container').forEach(cardContainer => {
            const productId = cardContainer.dataset.id;
            const quantityValueEl = cardContainer.querySelector('.quantity-value');

            if (cart[productId]) {
                cardContainer.querySelector('.product-card').classList.add('in-cart');
                quantityValueEl.textContent = cart[productId].quantity;
            } else {
                cardContainer.querySelector('.product-card').classList.remove('in-cart');
                quantityValueEl.textContent = '0';
            }
        });
    }

    function calculateTotals() {
        const subtotal = Object.values(cart).reduce((sum, item) => sum + (item.price * item.quantity), 0);
        const iva = subtotal * 0.16;
        const total = subtotal + iva;
        subtotalEl.textContent = `$${subtotal.toFixed(2)}`;
        ivaEl.textContent = `$${iva.toFixed(2)}`;
        totalEl.textContent = `$${total.toFixed(2)}`;
    }

    function handleCartAction(productId, action, productData = {}) {
        const existingItem = cart[productId];
        if (action === 'increase') {
            if (existingItem) {
                cart[productId].quantity++;
            } else { // Si no existe, lo añade con cantidad 1
                cart[productId] = { ...productData, quantity: 1 };
            }
        } else if (action === 'decrease') {
            if (existingItem) {
                cart[productId].quantity--;
                if (cart[productId].quantity <= 0) {
                    delete cart[productId];
                }
            }
        }
        renderViews(); // Llamamos a la función principal que actualiza todo
    }

    // Listener para los controles de cantidad en el catálogo
    if (productGrid) {
        productGrid.addEventListener('click', function (e) {
            const button = e.target.closest('button[data-action]');
            if (!button) return;

            const action = button.dataset.action;
            const productContainer = button.closest('.product-container');
            const productId = productContainer.dataset.id;

            const productData = {
                name: productContainer.dataset.name,
                description: productContainer.dataset.description,
                price: parseFloat(productContainer.dataset.price),
                image: productContainer.querySelector('img').src
            };

            handleCartAction(productId, action, productData);
        });
    }

    // Listener para los controles de cantidad en la factura
    if (invoiceItemsContainer) {
        invoiceItemsContainer.addEventListener('click', function (e) {
            const button = e.target.closest('button[data-action]');
            if (!button) return;
            const productId = button.dataset.id;
            const action = button.dataset.action;
            handleCartAction(productId, action);
        });
    }

    renderViews(); // Renderizado inicial
});