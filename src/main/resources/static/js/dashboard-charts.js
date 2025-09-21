document.addEventListener("DOMContentLoaded", () => {
    // Obtenemos los elementos canvas para los gráficos
    const salesCanvas = document.getElementById('salesChart');
    const productsCanvas = document.getElementById('productsChart');

    // --- Gráfica de Barras: Ventas de los últimos 7 días (DINÁMICA) ---
    // Solo se ejecuta si el elemento <canvas id="salesChart"> existe en la página actual
    if (salesCanvas) {
        const salesCtx = salesCanvas.getContext('2d');
        fetch('/dashboard/sales-last-7-days')
            .then(response => response.json())
            .then(data => {
                new Chart(salesCtx, {
                    type: 'bar',
                    data: {
                        labels: data.labels.map(label => label.charAt(0).toUpperCase() + label.slice(1)),
                        datasets: [{
                            label: 'Ventas ($)',
                            data: data.data,
                            backgroundColor: 'rgba(74, 20, 140, 0.6)',
                            borderColor: 'rgba(74, 20, 140, 1)',
                            borderWidth: 1
                        }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        scales: { y: { beginAtZero: true } }
                    }
                });
            })
            .catch(error => console.error('Error al cargar datos de ventas:', error));
    }

    // --- Gráfica de Pastel: Productos más vendidos (DINÁMICA) ---
    // Solo se ejecuta si el elemento <canvas id="productsChart"> existe en la página actual
    if (productsCanvas) {
        const productsCtx = productsCanvas.getContext('2d');
        fetch('/dashboard/top-selling-products')
            .then(response => response.json())
            .then(data => {
                new Chart(productsCtx, {
                    type: 'doughnut',
                    data: {
                        labels: data.labels,
                        datasets: [{
                            label: 'Cantidad Vendida',
                            data: data.data,
                            backgroundColor: [
                                'rgba(74, 20, 140, 0.8)',
                                'rgba(124, 77, 255, 0.8)',
                                'rgba(179, 157, 219, 0.8)',
                                'rgba(237, 231, 246, 0.9)',
                                'rgba(94, 53, 177, 0.8)'
                            ],
                            borderColor: '#ffffff'
                        }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                    }
                });
            })
            .catch(error => console.error('Error al cargar datos de productos:', error));
    }
});