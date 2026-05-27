function validarNumero(entrada) {
  limpiarError(entrada);
  var texto = entrada.value ? entrada.value.trim() : "";
  var minimo = Number(entrada.min || 1);
  var maximo = Number(entrada.max || Number.MAX_SAFE_INTEGER);
  if (texto === "") {
    mostrarError(entrada, "Ingrese un numero valido.");
    return false;
  }
  var valor = Number(texto);
  if (!Number.isInteger(valor) || valor < minimo || valor > maximo) {
    mostrarError(
      entrada,
      "Ingrese un valor entre " + minimo + " y " + maximo + ".",
    );
    return false;
  }
  entrada.value = String(valor);
  return true;
}

function validarDecimal(entrada) {
  limpiarError(entrada);
  var texto = entrada.value ? entrada.value.trim() : "";
  var minimo = Number(entrada.min || 0);
  if (texto === "") {
    mostrarError(entrada, "Ingrese un valor valido.");
    return false;
  }
  var valor = Number(texto);
  if (Number.isNaN(valor) || valor < minimo) {
    mostrarError(
      entrada,
      "Debe ingresar un valor mayor o igual a " + minimo + ".",
    );
    return false;
  }
  return true;
}

function validarSeleccion(entrada) {
  limpiarError(entrada);
  if (!entrada.value || entrada.value.trim() === "") {
    mostrarError(entrada, "Debe seleccionar una opcion.");
    return false;
  }
  return true;
}

function validarDNI(entrada) {
  limpiarError(entrada);
  var valor = entrada.value ? entrada.value.trim() : "";
  if (valor === "") {
    mostrarError(entrada, "El DNI es obligatorio.");
    return false;
  }
  if (valor.length < 8) {
    mostrarError(entrada, "El DNI debe tener al menos 8 digitos.");
    return false;
  }
  if (!/^\d+$/.test(valor)) {
    mostrarError(entrada, "El DNI solo puede contener numeros.");
    return false;
  }
  return true;
}

function validarDNIOpcional(entrada) {
  limpiarError(entrada);
  var valor = entrada.value ? entrada.value.trim() : "";
  if (valor === "") {
    return true;
  }
  if (valor.length < 8) {
    mostrarError(entrada, "El DNI debe tener al menos 8 digitos.");
    return false;
  }
  if (!/^\d+$/.test(valor)) {
    mostrarError(entrada, "El DNI solo puede contener numeros.");
    return false;
  }
  return true;
}

function validarEmail(entrada) {
  limpiarError(entrada);
  var valor = entrada.value ? entrada.value.trim() : "";
  if (valor === "") {
    mostrarError(entrada, "El email es obligatorio.");
    return false;
  }
  var patron = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!patron.test(valor)) {
    mostrarError(entrada, "Ingrese un email valido.");
    return false;
  }
  return true;
}

function validarTelefono(entrada) {
  limpiarError(entrada);
  var valor = entrada.value ? entrada.value.trim() : "";
  if (valor === "") {
    mostrarError(entrada, "El telefono es obligatorio.");
    return false;
  }
  if (valor.length < 9) {
    mostrarError(entrada, "El telefono debe tener al menos 9 digitos.");
    return false;
  }
  if (!/^\d+$/.test(valor)) {
    mostrarError(entrada, "El telefono solo puede contener numeros.");
    return false;
  }
  return true;
}
