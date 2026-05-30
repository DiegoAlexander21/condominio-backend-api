document.addEventListener('DOMContentLoaded', () => {
    const botonesEliminar = document.querySelectorAll('.confirmacion-eliminar-area');
    botonesEliminar.forEach(boton => {
        boton.addEventListener('click', (evento) => {
            const confirmacion = confirm('¿Estás seguro de que deseas eliminar esta área común? Se eliminarán también todas sus reservas asociadas.');
            if (!confirmacion) {
                evento.preventDefault();
            }
        });
    });
});
