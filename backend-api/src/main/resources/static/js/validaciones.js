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
