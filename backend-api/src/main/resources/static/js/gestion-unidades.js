// Validaciones específicas para el formulario de unidades

function actualizarEstadoResidente(select) {
  var residenteActivoSelect = document.getElementById("residenteActivo");
  if (!residenteActivoSelect) return;

  if (select.value === "activo") {
    residenteActivoSelect.value = "true";
  } else if (select.value === "inactivo") {
    residenteActivoSelect.value = "false";
  }
}

function actualizarEstadoDesdeSelect(select) {
  var tipoResidenteSelect = document.getElementById("tipoResidente");
  if (!tipoResidenteSelect) return;

  if (select.value === "true") {
    tipoResidenteSelect.value = "activo";
  } else if (select.value === "false") {
    tipoResidenteSelect.value = "inactivo";
  }
}

// Cargar el tipo de residente al editar
document.addEventListener("DOMContentLoaded", function() {
  var residenteActivoSelect = document.getElementById("residenteActivo");
  var tipoResidenteSelect = document.getElementById("tipoResidente");

  if (residenteActivoSelect && tipoResidenteSelect) {
    if (residenteActivoSelect.value === "true") {
      tipoResidenteSelect.value = "activo";
    } else {
      tipoResidenteSelect.value = "inactivo";
    }
  }
});

function validarFormularioUnidad(event) {
  // Validar condominio
  var condominio = document.getElementById("nombreCondominio");
  if (!condominio.value || condominio.value.trim() === "") {
    mostrarError(condominio, "Debe seleccionar un condominio.");
    return false;
  }

  // Validar número de unidad
  var numeroUnidad = document.getElementById("numeroUnidad");
  if (!validarTextoInput(numeroUnidad)) {
    return false;
  }

  // Validar torre
  var torre = document.getElementById("torre");
  if (!torre.value || torre.value.trim() === "") {
    mostrarError(torre, "Debe seleccionar una torre.");
    return false;
  }

  // Validar piso
  var piso = document.getElementById("piso");
  if (!normalizarNumero(piso)) {
    return false;
  }

  // Validar área
  var area = document.getElementById("area");
  if (!normalizarNumeroDecimal(area)) {
    return false;
  }

  // Validar propietario (obligatorio)
  var nombrePropietario = document.getElementById("nombrePropietario");
  if (!validarTextoInput(nombrePropietario)) {
    return false;
  }

  var dniPropietario = document.getElementById("dniPropietario");
  if (!validarDNI(dniPropietario)) {
    return false;
  }

  var telefonoPropietario = document.getElementById("telefonoPropietario");
  if (!validarTelefono(telefonoPropietario)) {
    return false;
  }

  var emailPropietario = document.getElementById("emailPropietario");
  if (!validarEmail(emailPropietario)) {
    return false;
  }

  // Validar residente (opcional, pero si hay nombre, validar DNI)
  var nombreResidente = document.getElementById("nombreResidente");
  if (nombreResidente.value && nombreResidente.value.trim() !== "") {
    var dniResidente = document.getElementById("dniResidente");
    if (!dniResidente.value || dniResidente.value.trim() === "") {
      mostrarError(dniResidente, "El DNI del residente es requerido si hay nombre.");
      return false;
    }
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

function validarDNIOpcional(input) {
  limpiarError(input);

  var valorTexto = input.value ? input.value.trim() : "";

  if (valorTexto === "") {
    return true; // Opcional, puede estar vacío
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

function validarTelefono(input) {
  limpiarError(input);

  var valorTexto = input.value ? input.value.trim() : "";

  if (valorTexto === "") {
    mostrarError(input, "El teléfono es obligatorio.");
    return false;
  }

  if (valorTexto.length < 9) {
    mostrarError(input, "El teléfono debe tener al menos 9 dígitos.");
    return false;
  }

  if (!/^\d+$/.test(valorTexto)) {
    mostrarError(input, "El teléfono solo puede contener números.");
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

  // Validación básica de email
  var patronEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!patronEmail.test(valorTexto)) {
    mostrarError(input, "Ingrese un email válido (ejemplo: usuario@email.com).");
    return false;
  }

  return true;
}

function validarTextoInputOpcional(input) {
  limpiarError(input);

  var valorTexto = input.value ? input.value.trim() : "";

  if (valorTexto === "") {
    return true; // Opcional
  }

  if (valorTexto.length < 3) {
    mostrarError(input, "Debe ingresar al menos 3 caracteres.");
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
