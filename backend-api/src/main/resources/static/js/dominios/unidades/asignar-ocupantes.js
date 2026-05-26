function limpiarTodo() {
    if (confirm('¿Estás seguro de que quieres vaciar todos los campos del propietario y residente? (Los cambios no se guardarán hasta hacer clic en "Guardar")')) {
        document.getElementById('nombrePropietario').value = '';
        document.getElementById('dniPropietario').value = '';
        document.getElementById('emailPropietario').value = '';
        document.getElementById('telefonoPropietario').value = '';
        
        limpiarCamposResidente();
    }
}

function limpiarResidente() {
    if (confirm('¿Estás seguro de que quieres vaciar los datos del residente?')) {
        limpiarCamposResidente();
    }
}

function limpiarCamposResidente() {
    document.getElementById('nombreResidente').value = '';
    document.getElementById('dniResidente').value = '';
    document.getElementById('emailResidente').value = '';
    document.getElementById('parentesco').value = '';
    document.getElementById('residenteActivo').value = 'false';
}

function revertirCambios() {
    if (confirm('¿Estás seguro de que deseas deshacer los cambios no guardados y restaurar los valores originales?')) {
        document.getElementById('formularioOcupantes').reset();
    }
}
