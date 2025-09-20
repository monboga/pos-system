document.addEventListener("DOMContentLoaded", function () {
    const salesTableBody = document.getElementById('sales-history-body');
    const saleDetailModal = document.getElementById('saleDetailModal');
    const filterTypeSelect = document.getElementById('filterType');
    const dateFilter = document.getElementById('dateFilter');
    const saleNumberFilter = document.getElementById('saleNumberFilter');

    if (filterTypeSelect && dateFilter && saleNumberFilter) {
        filterTypeSelect.addEventListener('change', function () {
            if (this.value === 'date') {
                dateFilter.classList.remove('d-none');
                saleNumberFilter.classList.add('d-none');
            } else if (this.value === 'saleNumber') {
                dateFilter.classList.add('d-none');
                saleNumberFilter.classList.remove('d-none');
            }
        });
    }

    if (salesTableBody && saleDetailModal) {
        salesTableBody.addEventListener('click', function (event) {
            const viewButton = event.target.closest('.view-sale-btn');
            if (!viewButton) return;

            const saleRow = viewButton.closest('tr');
            const saleId = saleRow.dataset.saleId;

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
});