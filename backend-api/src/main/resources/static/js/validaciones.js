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

function validarNumero(entrada) {
  limpiarError(entrada);
  var texto = entrada.value ? entrada.value.trim() : "";
  var minimo = Number(entrada.min || 1);
  var maximo = Number(entrada.max || Number.MAX_SAFE_INTEGER);
  if (texto === "") {
    mostrarError(entrada, "Ingrese un número válido.");
    return false;
  }
  var valor = Number(texto);
  if (!Number.isInteger(valor) || valor < minimo || valor > maximo) {
    mostrarError(entrada, "Ingrese un valor entre " + minimo + " y " + maximo + ".");
    return false;
  }
  entrada.value = String(valor);
  return true;
}
