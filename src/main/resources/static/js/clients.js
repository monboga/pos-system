document.addEventListener("DOMContentLoaded", function () {
    const clientsTableBody = document.getElementById('clients-table-body');
    const clientModalLabel = document.getElementById('clientModalLabel');
    const clientForm = document.getElementById('clientForm');
    const addClientBtn = document.getElementById('addClientBtn');

    function populateEditModal(editButton) {
        const clientRow = editButton.closest('tr');
        const clientData = clientRow.dataset;

        clientModalLabel.textContent = 'Editar Cliente';

        document.getElementById('clientId').value = clientData.id;
        document.getElementById('clientRfc').value = clientData.rfc;
        document.getElementById('clientNombre').value = clientData.nombre;
        document.getElementById('clientCorreo').value = clientData.correo;
        document.getElementById('clientRegimenFiscal').value = clientData.regimenFiscalId;
        document.getElementById('clientActivo').value = clientData.activo;
    }

    function prepareAddModal() {
        clientModalLabel.textContent = 'Agregar Nuevo Cliente';
        clientForm.reset();
        document.getElementById('clientId').value = '';
    }

    if (clientsTableBody) {
        clientsTableBody.addEventListener('click', function (event) {
            const editButton = event.target.closest('.edit-client-btn');
            if (editButton) {
                populateEditModal(editButton);
            }
        });
    }

    if (addClientBtn) {
        addClientBtn.addEventListener('click', function () {
            prepareAddModal();
        });
    }
});