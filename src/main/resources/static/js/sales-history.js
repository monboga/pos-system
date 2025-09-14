document.addEventListener("DOMContentLoaded", function () {
    const filterTypeSelect = document.getElementById('filterType');
    const dateFilter = document.getElementById('dateFilter');
    const saleNumberFilter = document.getElementById('saleNumberFilter');

    if (filterTypeSelect) {
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
});