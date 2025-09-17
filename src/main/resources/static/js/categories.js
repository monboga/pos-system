document.addEventListener("DOMContentLoaded", function () {
    const categoriesTableBody = document.getElementById('categories-table-body');
    const categoryModalLabel = document.getElementById('categoryModalLabel');
    const categoryForm = document.getElementById('categoryForm');
    const addCategoryBtn = document.getElementById('addCategoryBtn');

    function populateEditModal(editButton) {
        const categoryRow = editButton.closest('tr');
        const categoryData = categoryRow.dataset;

        categoryModalLabel.textContent = 'Editar Categoría';

        document.getElementById('categoryId').value = categoryData.id;
        document.getElementById('categoryDescription').value = categoryData.descripcion;
        document.getElementById('categoryStatus').value = categoryData.estado;
    }

    function prepareAddModal() {
        categoryModalLabel.textContent = 'Agregar Nueva Categoría';
        categoryForm.reset();
        document.getElementById('categoryId').value = '';
    }

    if (categoriesTableBody) {
        categoriesTableBody.addEventListener('click', function (event) {
            const editButton = event.target.closest('.edit-category-btn');
            if (editButton) {
                populateEditModal(editButton);
            }
        });
    }

    if (addCategoryBtn) {
        addCategoryBtn.addEventListener('click', function () {
            prepareAddModal();
        });
    }
});