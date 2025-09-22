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
                            backgroundColor: 'rgba(126, 87, 194, 0.6)', // Tono lavanda con transparencia
                            borderColor: 'rgba(126, 87, 194, 1)',
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
                                'rgba(126, 87, 194, 0.8)', // Lavanda Principal (#7E57C2)
                                'rgba(94, 53, 177, 0.8)',  // Púrpura Oscuro (#5E35B1)
                                'rgba(179, 157, 219, 0.8)',// Lavanda Claro (#B39DDB)
                                'rgba(237, 231, 246, 0.9)',// Lavanda Pálido (#EDE7F6)
                                'rgba(149, 117, 205, 0.8)' // Lavanda Medio (#9575CD)
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