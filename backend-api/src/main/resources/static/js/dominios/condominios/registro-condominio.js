document.addEventListener("DOMContentLoaded", function () {
  var formulario = document.getElementById("formularioCondominio");
  var nombre = document.getElementById("nombreCondominio");
  var torres = document.getElementById("torres");
  var pisos = document.getElementById("pisosPorTorre");

  if (!formulario || !nombre || !torres || !pisos) {
    return;
  }

  nombre.addEventListener("blur", function () {
    if (nombre.value !== "") {
      validarTexto(nombre);
    }
  });

  torres.addEventListener("input", function () {
    torres.value = torres.value.replace(/[^\d]/g, "");
    limpiarError(torres);
  });

  pisos.addEventListener("input", function () {
    pisos.value = pisos.value.replace(/[^\d]/g, "");
    limpiarError(pisos);
  });

  formulario.addEventListener("submit", function (evento) {
    var nombreValido = validarTexto(nombre);
    var torresValidas = validarNumero(torres);
    var pisosValidos = validarNumero(pisos);

    if (!(nombreValido && torresValidas && pisosValidos)) {
      evento.preventDefault();
    }
  });
});
