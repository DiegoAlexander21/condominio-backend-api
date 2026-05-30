document.addEventListener('DOMContentLoaded', () => {
    const formulariosEliminar = document.querySelectorAll('.confirmacion-eliminar-gasto');
    formulariosEliminar.forEach(formulario => {
        formulario.addEventListener('submit', (evento) => {
            const confirmacion = confirm('¿Está seguro de eliminar este gasto y toda su distribución?');
            if (!confirmacion) {
                evento.preventDefault();
            }
        });
    });
});
