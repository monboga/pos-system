document.addEventListener("DOMContentLoaded", function () {
    // --- ESTADO DE LA APLICACIÓN ---
    let cart = {};
    let activeCategories = []; // Array para guardar las categorías activas
    let searchTerm = ''; // String para el término de búsqueda

    // --- SELECTORES DEL DOM ---
    const productGrid = document.getElementById('product-grid');
    const invoiceItemsContainer = document.getElementById('invoice-items');
    const subtotalEl = document.getElementById('subtotal');
    const ivaEl = document.getElementById('iva');
    const totalEl = document.getElementById('total');

    // Selectores para los nuevos filtros
    const categoryCardsContainer = document.getElementById('category-cards-container');
    const clearCategoryFiltersBtn = document.getElementById('clear-category-filters');
    const productSearchInput = document.getElementById('product-search-input');

    // --- LÓGICA DE FILTRADO ---

    /**
     * Función central que aplica TODOS los filtros al catálogo de productos.
     */
    function applyFilters() {
        const allProducts = productGrid.querySelectorAll('.product-container');

        allProducts.forEach(product => {
            const productName = product.dataset.name.toLowerCase();
            const productCategory = product.dataset.categoryName;

            // Condición 1: Filtrar por término de búsqueda
            const nameMatch = productName.includes(searchTerm);

            // Condición 2: Filtrar por categoría
            // El producto se muestra si no hay categorías activas O si su categoría está en la lista de activas
            const categoryMatch = activeCategories.length === 0 || activeCategories.includes(productCategory);

            // El producto es visible solo si cumple AMBAS condiciones
            if (nameMatch && categoryMatch) {
                product.style.display = 'block';
            } else {
                product.style.display = 'none';
            }
        });
    }

    /**
     * Actualiza la clase 'active' en las tarjetas de categoría según el estado.
     */
    function updateCategoryCardsVisualState() {
        const allCategoryCards = categoryCardsContainer.querySelectorAll('.category-card');
        allCategoryCards.forEach(card => {
            const cardCategoryName = card.dataset.categoryName;
            // La tarjeta "Todas" está activa si no hay otras categorías seleccionadas
            if (cardCategoryName === 'all') {
                card.classList.toggle('active', activeCategories.length === 0);
            } else {
                // Las demás tarjetas están activas si su nombre está en el array
                card.classList.toggle('active', activeCategories.includes(cardCategoryName));
            }
        });
    }

    // --- LÓGICA DEL CARRITO ---

    function renderCart() {
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
        updateCatalogView(); // Sincronizamos el catálogo con el carrito
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
            } else {
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
        renderCart();
    }

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

    // --- EVENT LISTENERS ---

    if (productSearchInput) {
        productSearchInput.addEventListener('input', function (e) {
            searchTerm = e.target.value.toLowerCase();
            applyFilters();
        });
    }

    if (categoryCardsContainer) {
        categoryCardsContainer.addEventListener('click', function (e) {
            const clickedCard = e.target.closest('.category-card');
            if (!clickedCard) return;

            e.preventDefault();
            const categoryName = clickedCard.dataset.categoryName;

            if (categoryName === 'all') {
                activeCategories = [];
            } else {
                const index = activeCategories.indexOf(categoryName);
                if (index > -1) {
                    activeCategories.splice(index, 1);
                } else {
                    activeCategories.push(categoryName);
                }
            }

            updateCategoryCardsVisualState();
            applyFilters();
        });
    }

    if (clearCategoryFiltersBtn) {
        clearCategoryFiltersBtn.addEventListener('click', function () {
            activeCategories = [];
            updateCategoryCardsVisualState();
            applyFilters();
        });
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
                image: productContainer.dataset.image
            };

            handleCartAction(productId, action, productData);
        });
    }

    if (invoiceItemsContainer) {
        invoiceItemsContainer.addEventListener('click', function (e) {
            const button = e.target.closest('button[data-action]');
            if (!button) return;

            const productId = button.dataset.id;
            const action = button.dataset.action;
            handleCartAction(productId, action);
        });
    }

    // Renderizado inicial
    renderCart();
});