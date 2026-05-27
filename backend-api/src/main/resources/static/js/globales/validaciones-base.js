function limpiarError(campo) {
  campo.setCustomValidity("");
}

function mostrarError(campo, mensaje) {
  campo.setCustomValidity(mensaje);
  campo.reportValidity();
  campo.focus();
}

function validarTexto(entrada) {
  limpiarError(entrada);
  var valor = entrada.value ? entrada.value.trim() : "";
  if (valor.length < 2) {
    mostrarError(entrada, "Debe ingresar al menos 2 caracteres.");
    return false;
  }
  entrada.value = valor;
  return true;
}

function validarTextoOpcional(entrada) {
  limpiarError(entrada);
  var valor = entrada.value ? entrada.value.trim() : "";
  if (valor === "") {
    return true;
  }
  if (valor.length < 3) {
    mostrarError(entrada, "Debe ingresar al menos 3 caracteres.");
    return false;
  }
  return true;
}
