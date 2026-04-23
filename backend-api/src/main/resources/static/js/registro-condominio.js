// registro-condominio.js — lógica del formulario de registro
// Requiere: validaciones.js cargado antes que este archivo

function prepararInputNumerico(input) {
  if (!input) return;

  input.addEventListener("focus", function () {
    if (input.value === "0") input.value = "";
  });

  input.addEventListener("input", function () {
    var texto = input.value ? input.value.trim() : "";
    if (/^0+\d+$/.test(texto)) input.value = String(Number(texto));
  });
}

function validarFormularioCondominio(event) {
  var nombre = document.getElementById("nombreCondominio");
  var torres = document.getElementById("torres");
  var pisos = document.getElementById("pisosPorTorre");

  if (
    !validarTextoInput(nombre) ||
    !normalizarNumero(torres) ||
    !normalizarNumero(pisos)
  ) {
    event.preventDefault();
    return false;
  }

  return true;
}

document.addEventListener("DOMContentLoaded", function () {
  prepararInputNumerico(document.getElementById("torres"));
  prepararInputNumerico(document.getElementById("pisosPorTorre"));
});
