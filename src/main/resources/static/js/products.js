document.addEventListener("DOMContentLoaded", function () {
    // Selectores del DOM
    const productsTableBody = document.getElementById('products-table-body');
    const productModalLabel = document.getElementById('productModalLabel');
    const productForm = document.getElementById('productForm');
    const addProductBtn = document.getElementById('addProductBtn');

    const photoPreviewImg = document.getElementById('photo-preview-img');
    const photoUploadInput = document.getElementById('photo-upload');

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
        document.getElementById('productPrice').value = productData.precioUnitario;
        document.getElementById('productDiscount').value = productData.descuento;
        document.getElementById('productStatus').value = productData.estado;
        document.getElementById('productUnit').value = productData.medidaLocalId;
        document.getElementById('productUnitSat').value = productData.medidaSatId;
        document.getElementById('productKeySat').value = productData.claveProdServSatId;
        document.getElementById('productTaxObject').value = productData.objetoImpSatId;
        document.getElementById('productTax').value = productData.impuestoSatId;

        // Mostrar la imagen actual del producto
        if (productData.imagen && productData.imagen !== 'null') {
            photoPreviewImg.src = productData.imagen;
        } else {
            photoPreviewImg.src = 'https://via.placeholder.com/200';
        }
    }

    /**
     * Prepara el modal para agregar un nuevo producto (limpia el formulario).
     */
    function prepareAddModal() {
        productModalLabel.textContent = 'Agregar Nuevo Producto';
        productForm.reset();
        document.getElementById('productId').value = ''; // Asegura que el ID esté vacío
        photoPreviewImg.src = 'https://via.placeholder.com/200';
    }

    // Listener para la previsualización de la imagen
    if (photoUploadInput) {
        photoUploadInput.addEventListener('change', function (event) {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    photoPreviewImg.src = e.target.result;
                };
                reader.readAsDataURL(file);
            }
        });
    }

    // Listeners para los botones de la tabla y "Agregar"
    if (productsTableBody) {
        productsTableBody.addEventListener('click', function (event) {
            const editButton = event.target.closest('.edit-product-btn');
            if (editButton) {
                populateEditModal(editButton);
            }
        });
    }

    if (addProductBtn) {
        addProductBtn.addEventListener('click', function () {
            prepareAddModal();
        });
    }
});