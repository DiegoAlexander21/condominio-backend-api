// Validaciones reutilizables

function limpiarError(campo) {
  campo.setCustomValidity("");
}

function mostrarError(campo, mensaje) {
  campo.setCustomValidity(mensaje);
  campo.reportValidity();
  campo.focus();
}

function validarTextoInput(input) {
  limpiarError(input);

  if (!input.value || input.value.trim().length < 2) {
    mostrarError(input, "Debe ingresar al menos 2 caracteres.");
    return false;
  }

  return true;
}

function normalizarNumero(input) {
  limpiarError(input);

  var valorTexto = input.value ? input.value.trim() : "";
  var minimo = Number(input.min || 0);

  if (valorTexto === "") {
    mostrarError(input, "Ingrese un número válido.");
    return false;
  }

  var valor = Number(valorTexto);

  if (Number.isNaN(valor) || valor < minimo) {
    mostrarError(
      input,
      "Debe ingresar un valor mayor o igual a " + minimo + ".",
    );
    return false;
  }

  return true;
}
