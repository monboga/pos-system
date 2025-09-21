document.addEventListener("DOMContentLoaded", function () {
    // Selectores del DOM
    const productsTableBody = document.getElementById('products-table-body');
    const addProductBtn = document.getElementById('addProductBtn');
    const productModalEl = document.getElementById('productModal');
    const productModalLabel = document.getElementById('productModalLabel');
    const productForm = document.getElementById('productForm');

    const photoPreviewImg = document.getElementById('photo-preview-img');
    const photoPreviewPlaceholder = document.getElementById('photo-preview-placeholder');
    const photoUploadInput = document.getElementById('photo-upload');

    // Selectores para campos con formato
    const priceInput = document.getElementById('productPrice');
    const discountInput = document.getElementById('productDiscount');

    /**
     * Formatea un número como moneda MXN.
     * @param {number|string} value - El valor numérico a formatear.
     * @returns {string} - El valor formateado (ej: "$ 100.00 MXN").
     */
    function formatPrice(value) {
        const num = parseFloat(value) || 0;
        return num.toLocaleString('es-MX', { style: 'currency', currency: 'MXN' });
    }

    /**
     * Formatea un número como porcentaje.
     * @param {number|string} value - El valor numérico a formatear.
     * @returns {string} - El valor formateado (ej: "5 %").
     */
    function formatDiscount(value) {
        const num = parseFloat(value) || 0;
        return `${num} %`;
    }

    /**
     * Extrae el valor numérico de una cadena formateada.
     * @param {string} value - La cadena formateada.
     * @returns {string} - El número como cadena de texto.
     */
    function unformatValue(value) {
        // Extrae todos los dígitos, el punto decimal y el signo negativo.
        const matches = value.match(/[\d.-]+/g);
        return matches ? matches.join('') : '0';
    }

    function showImagePreview(src) {
        photoPreviewImg.src = src;
        photoPreviewImg.classList.remove('d-none');
        photoPreviewPlaceholder.classList.add('d-none');
    }

    function showPlaceholder() {
        photoPreviewImg.src = '';
        photoPreviewImg.classList.add('d-none');
        photoPreviewPlaceholder.classList.remove('d-none');
    }

    /**
     * Prepara y llena el modal para editar un producto.
     * @param {Element} editButton - El botón de editar que fue presionado.
     */
    function populateEditModal(editButton) {
        const productRow = editButton.closest('tr');
        const productData = productRow.dataset;

        productModalLabel.textContent = 'Editar Producto';

        // Llenar todos los campos del formulario desde los atributos data-* de la fila
        document.getElementById('productId').value = productData.id;
        document.getElementById('productDescription').value = productData.descripcion;
        document.getElementById('productBrand').value = productData.marca;
        document.getElementById('productBarcode').value = productData.codigoDeBarra;
        document.getElementById('productCategory').value = productData.categoryId;
        document.getElementById('productStock').value = productData.stock;
        document.getElementById('productStatus').value = productData.estado;
        document.getElementById('productUnit').value = productData.medidaLocalId;
        document.getElementById('productUnitSat').value = productData.medidaSatId;
        document.getElementById('productKeySat').value = productData.claveProdServSatId;
        document.getElementById('productTaxObject').value = productData.objetoImpSatId;
        document.getElementById('productTax').value = productData.impuestoSatId;

        priceInput.value = formatPrice(productData.precioUnitario);
        discountInput.value = formatDiscount(productData.descuento);

        // Mostrar la imagen actual del producto
        if (productData.imagen && productData.imagen !== 'null') {
            showImagePreview(productData.imagen);
        } else {
            showPlaceholder();
        }
    }

    /**
     * Prepara el modal para agregar un nuevo producto (limpia el formulario).
     */
    function prepareAddModal() {
        productModalLabel.textContent = 'Agregar Nuevo Producto';
        productForm.reset();
        document.getElementById('productId').value = ''; // Asegura que el ID esté vacío
        showPlaceholder();

        discountInput.value = formatDiscount(0);
        priceInput.value = formatPrice(0);

        // Establecer valores por defecto para el modo "Agregar"
        document.getElementById('productUnit').value = 'PZA'; // Unidad de Medida (Empresa): Pieza
        document.getElementById('productUnitSat').value = 'Unidades de venta';  // Unidad de Medida (SAT): Unidades de Venta
        document.getElementById('productKeySat').value = '01010101'; // Clave del Producto (SAT): No Existe en el Catalogo
        document.getElementById('productTaxObject').value = '02'; // Objeto del Impuesto: Si Objeto de Impuesto
        document.getElementById('productTax').value = '002'; // Impuesto: IVA
    }

    // Formatear al salir del campo
    priceInput.addEventListener('blur', () => {
        priceInput.value = formatPrice(priceInput.value);
    });
    discountInput.addEventListener('blur', () => {
        discountInput.value = formatDiscount(discountInput.value);
    });

    // Des-formatear al entrar al campo para facilitar la edición
    priceInput.addEventListener('focus', () => {
        priceInput.value = unformatValue(priceInput.value);
    });
    discountInput.addEventListener('focus', () => {
        discountInput.value = unformatValue(discountInput.value);
    });

    // Antes de enviar el formulario, limpiar el formato para enviar solo números
    productForm.addEventListener('submit', () => {
        priceInput.value = unformatValue(priceInput.value);
        discountInput.value = unformatValue(discountInput.value);
    });

    // Listener para la previsualización de la imagen
    if (photoUploadInput) {
        photoUploadInput.addEventListener('change', function (event) {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    showImagePreview(e.target.result);
                };
                reader.readAsDataURL(file);
            }
        });
    }

    // Listeners para los botones de la tabla y "Agregar"
    if (productModalEl) {
        productModalEl.addEventListener('show.bs.modal', function (event) {
            // Obtenemos el botón que disparó el modal
            const button = event.relatedTarget;

            // Verificamos si el botón es de 'editar' (tiene la clase 'edit-product-btn')
            if (button && button.classList.contains('edit-product-btn')) {
                populateEditModal(button);
            } else {
                // Si no, asumimos que es el botón de 'agregar' o se abrió de otra forma
                prepareAddModal();
            }
        });
    }
});