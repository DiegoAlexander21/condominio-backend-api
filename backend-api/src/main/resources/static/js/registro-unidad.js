document.addEventListener("DOMContentLoaded", function () {
  var formulario = document.getElementById("formularioUnidad");
  var condominio = document.getElementById("nombreCondominio");
  var numeroUnidad = document.getElementById("numeroUnidad");
  var torre = document.getElementById("torre");
  var piso = document.getElementById("piso");
  var area = document.getElementById("area");
  var nombrePropietario = document.getElementById("nombrePropietario");
  var dniPropietario = document.getElementById("dniPropietario");
  var emailPropietario = document.getElementById("emailPropietario");
  var telefonoPropietario = document.getElementById("telefonoPropietario");
  var nombreResidente = document.getElementById("nombreResidente");
  var dniResidente = document.getElementById("dniResidente");

  if (!formulario) {
    return;
  }

  numeroUnidad.addEventListener("blur", function () {
    if (numeroUnidad.value !== "") {
      validarTexto(numeroUnidad);
    }
  });

  nombrePropietario.addEventListener("blur", function () {
    if (nombrePropietario.value !== "") {
      validarTexto(nombrePropietario);
    }
  });

  dniPropietario.addEventListener("input", function () {
    dniPropietario.value = dniPropietario.value.replace(/[^\d]/g, "");
    limpiarError(dniPropietario);
  });

  telefonoPropietario.addEventListener("input", function () {
    telefonoPropietario.value = telefonoPropietario.value.replace(/[^\d]/g, "");
    limpiarError(telefonoPropietario);
  });

  piso.addEventListener("input", function () {
    piso.value = piso.value.replace(/[^\d]/g, "");
    limpiarError(piso);
  });

  condominio.addEventListener("change", function () {
    limpiarError(condominio);
  });

  torre.addEventListener("change", function () {
    limpiarError(torre);
  });

  formulario.addEventListener("submit", function (evento) {
    var condominioValido = validarSeleccion(condominio);
    if (!condominioValido) { evento.preventDefault(); return; }

    var unidadValida = validarTexto(numeroUnidad);
    if (!unidadValida) { evento.preventDefault(); return; }

    var torreValida = validarSeleccion(torre);
    if (!torreValida) { evento.preventDefault(); return; }

    var pisoValido = validarNumero(piso);
    if (!pisoValido) { evento.preventDefault(); return; }

    var areaValida = validarDecimal(area);
    if (!areaValida) { evento.preventDefault(); return; }

    var propietarioValido = validarTexto(nombrePropietario);
    if (!propietarioValido) { evento.preventDefault(); return; }

    var dniValido = validarDNI(dniPropietario);
    if (!dniValido) { evento.preventDefault(); return; }

    var emailValido = validarEmail(emailPropietario);
    if (!emailValido) { evento.preventDefault(); return; }

    var telefonoValido = validarTelefono(telefonoPropietario);
    if (!telefonoValido) { evento.preventDefault(); return; }

    if (nombreResidente.value && nombreResidente.value.trim() !== "") {
      var residenteValido = validarTextoOpcional(nombreResidente);
      if (!residenteValido) { evento.preventDefault(); return; }

      var dniResidenteValido = validarDNIOpcional(dniResidente);
      if (!dniResidenteValido) { evento.preventDefault(); return; }
    }
  });
});
