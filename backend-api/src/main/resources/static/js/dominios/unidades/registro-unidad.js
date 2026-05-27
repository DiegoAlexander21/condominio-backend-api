document.addEventListener("DOMContentLoaded", function () {
  var formulario = document.getElementById("formularioUnidad");
  var condominio = document.getElementById("nombreCondominio");
  var numeroUnidad = document.getElementById("numeroUnidad");
  var torre = document.getElementById("torre");
  var piso = document.getElementById("piso");
  var area = document.getElementById("area");

  if (!formulario) return;

  numeroUnidad.addEventListener("blur", function () {
    if (numeroUnidad.value !== "") validarTexto(numeroUnidad);
  });

  piso.addEventListener("input", function () {
    piso.value = piso.value.replace(/[^\d]/g, "");
    limpiarError(piso);
  });

  function actualizarTorres() {
    var valorTorreSeleccionada =
      torre.getAttribute("data-selected") || torre.value;
    torre.innerHTML = '<option value="">Seleccione torre</option>';
    var opcionSeleccionada = condominio.options[condominio.selectedIndex];

    if (opcionSeleccionada && opcionSeleccionada.value !== "") {
      torre.disabled = false;
      var numTorres = parseInt(
        opcionSeleccionada.getAttribute("data-torres"),
        10,
      );
      for (var i = 1; i <= numTorres; i++) {
        var opcion = document.createElement("option");
        opcion.value = "Torre " + i;
        opcion.textContent = "Torre " + i;
        if (opcion.value === valorTorreSeleccionada) {
          opcion.selected = true;
        }
        torre.appendChild(opcion);
      }
    } else {
      torre.disabled = true;
    }
  }

  condominio.addEventListener("change", function () {
    limpiarError(condominio);
    torre.setAttribute("data-selected", "");
    actualizarTorres();
  });

  if (condominio.value !== "") {
    actualizarTorres();
  }

  torre.addEventListener("change", function () {
    limpiarError(torre);
  });

  formulario.addEventListener("submit", function (evento) {
    if (!validarSeleccion(condominio)) {
      evento.preventDefault();
      return;
    }
    if (!validarTexto(numeroUnidad)) {
      evento.preventDefault();
      return;
    }
    if (!validarSeleccion(torre)) {
      evento.preventDefault();
      return;
    }
    if (!validarNumero(piso)) {
      evento.preventDefault();
      return;
    }
    if (!validarDecimal(area)) {
      evento.preventDefault();
      return;
    }
  });
});
