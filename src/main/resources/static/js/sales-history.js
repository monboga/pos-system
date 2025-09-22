document.addEventListener("DOMContentLoaded", function () {
    const salesTableBody = document.getElementById('sales-history-body');
    const saleDetailModal = document.getElementById('saleDetailModal');
    const filterTypeSelect = document.getElementById('filterType');
    const dateFilter = document.getElementById('dateFilter');
    const startDateInput = document.getElementById('startDate');
    const endDateInput = document.getElementById('endDate');
    const saleNumberFilter = document.getElementById('saleNumberFilter');
    const saleNumberInput = document.getElementById('saleNumberInput');
    const printButton = document.getElementById('printSaleBtn');

    // Función para manejar la visibilidad y estado de los filtros
    function toggleFilters() {
        if (filterTypeSelect.value === 'date') {
            dateFilter.classList.remove('d-none');
            startDateInput.disabled = false;
            endDateInput.disabled = false;

            saleNumberFilter.classList.add('d-none');
            saleNumberInput.disabled = true;
        } else if (filterTypeSelect.value === 'saleNumber') {
            dateFilter.classList.add('d-none');
            startDateInput.disabled = true;
            endDateInput.disabled = true;

            saleNumberFilter.classList.remove('d-none');
            saleNumberInput.disabled = false;
        }
    }

    if (filterTypeSelect) {
        // Ejecutar al cargar la página para establecer el estado inicial correcto
        toggleFilters();

        filterTypeSelect.addEventListener('change', toggleFilters);
    }

    if (salesTableBody && saleDetailModal) {
        salesTableBody.addEventListener('click', function (event) {
            const viewButton = event.target.closest('.view-sale-btn');
            if (!viewButton) return;

            const saleRow = viewButton.closest('tr');
            const saleId = saleRow.dataset.saleId;

            // 1. Guardamos el ID de la venta en un atributo data-* del botón "Imprimir".
            if (printButton) {
                printButton.dataset.saleId = saleId;
            }

            // Hacemos la petición fetch al backend
            fetch(`/sales-history/details/${saleId}`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('No se pudo obtener el detalle de la venta.');
                    }
                    return response.json();
                })
                .then(data => {
                    // Poblamos los campos del modal con los datos recibidos
                    document.getElementById('saleDetailModalLabel').textContent = `Detalle de Venta: ${data.saleNumber}`;
                    document.getElementById('modal-doc-type').textContent = data.documentType;
                    document.getElementById('modal-client-name').textContent = data.clientName;
                    document.getElementById('modal-client-rfc').textContent = data.clientRfc;

                    const productsBody = document.getElementById('modal-products-body');
                    productsBody.innerHTML = ''; // Limpiamos la tabla

                    data.details.forEach(detail => {
                        const row = `
                            <tr>
                                <td>${detail.productName}</td>
                                <td>${detail.quantity}</td>
                                <td>$${detail.price.toFixed(2)}</td>
                                <td>$${detail.total.toFixed(2)}</td>
                            </tr>
                        `;
                        productsBody.insertAdjacentHTML('beforeend', row);
                    });

                    document.getElementById('modal-subtotal').textContent = `$${data.subtotal.toFixed(2)}`;
                    document.getElementById('modal-iva').textContent = `$${data.iva.toFixed(2)}`;
                    document.getElementById('modal-total').textContent = `$${data.total.toFixed(2)}`;
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('Error al cargar los detalles. Por favor, intente de nuevo.');
                });
        });
    }

    if (printButton) {
        printButton.addEventListener('click', function () {
            // Leemos el ID que guardamos en el paso 1.
            const saleIdToPrint = this.dataset.saleId;

            if (saleIdToPrint) {
                // Si encontramos el ID, abrimos la URL del reporte en una nueva pestaña.
                window.open(`/sales/ticket/${saleIdToPrint}`, '_blank');
            } else {
                // Si no, mostramos el error que estabas viendo.
                alert('No se pudo encontrar el ID de la venta para imprimir.');
            }
        });
    }
});