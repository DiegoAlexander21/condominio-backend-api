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

function normalizarNumeroDecimal(input) {
  limpiarError(input);

  var valorTexto = input.value ? input.value.trim() : "";
  var minimo = Number(input.min || 0);

  if (valorTexto === "") {
    mostrarError(input, "Ingrese un valor válido.");
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

function validarDNI(input) {
  limpiarError(input);

  var valorTexto = input.value ? input.value.trim() : "";

  if (valorTexto === "") {
    mostrarError(input, "El DNI es obligatorio.");
    return false;
  }

  if (valorTexto.length < 8) {
    mostrarError(input, "El DNI debe tener al menos 8 dígitos.");
    return false;
  }

  if (!/^\d+$/.test(valorTexto)) {
    mostrarError(input, "El DNI solo puede contener números.");
    return false;
  }

  return true;
}

function validarEmail(input) {
  limpiarError(input);

  var valorTexto = input.value ? input.value.trim() : "";

  if (valorTexto === "") {
    mostrarError(input, "El email es obligatorio.");
    return false;
  }

  var patronEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!patronEmail.test(valorTexto)) {
    mostrarError(input, "Ingrese un email válido (ejemplo: usuario@email.com).");
    return false;
  }

  return true;
}
