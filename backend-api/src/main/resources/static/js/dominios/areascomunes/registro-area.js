document.addEventListener("DOMContentLoaded", function () {
    const formulario = document.getElementById("formularioArea");
    const entradaNombre = document.getElementById("nombre");
    const entradaCapacidad = document.getElementById("capacidad");

    formulario.addEventListener("submit", function (evento) {
        let esValido = true;

        if (!validarTexto(entradaNombre)) {
            esValido = false;
        }

        if (!validarNumero(entradaCapacidad)) {
            esValido = false;
        }

        if (!esValido) {
            evento.preventDefault();
        }
    });
});
