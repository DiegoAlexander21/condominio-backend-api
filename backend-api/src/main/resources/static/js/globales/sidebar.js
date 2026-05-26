function cerrarSesion() {
    eliminarToken();
}

document.addEventListener('DOMContentLoaded', function() {
    const rutaActual = window.location.pathname;
    const menuFinanzas = document.getElementById('menu-finanzas');
    
    if (rutaActual.startsWith('/finanzas') && menuFinanzas) {
        menuFinanzas.open = true;
    }
});
