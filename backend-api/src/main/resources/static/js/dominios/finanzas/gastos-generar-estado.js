function inicializarGeneracionEstadoCuenta() {
  var condominioGenerar = document.getElementById("condominioGenerarId");
  var torreGenerar = document.getElementById("torreGenerarId");
  var unidadGenerar = document.getElementById("unidadId");

  if (!condominioGenerar || !torreGenerar || !unidadGenerar) {
    return;
  }

  var opcionesOriginales = Array.from(unidadGenerar.options).filter(
    function (opcion) {
      return opcion.value !== "";
    },
  );

  function actualizarTorresYUnidades() {
    var condominioSeleccionado = condominioGenerar.value;

    torreGenerar.innerHTML = '<option value="">Seleccione Torre...</option>';
    unidadGenerar.innerHTML = '<option value="">Seleccione...</option>';

    if (!condominioSeleccionado) {
      torreGenerar.disabled = true;
      unidadGenerar.disabled = true;
      return;
    }

    torreGenerar.disabled = false;
    unidadGenerar.disabled = true;

    var torresUnicas = new Set();
    var unidadesFiltradas = opcionesOriginales.filter(function (opcion) {
      return opcion.getAttribute("data-condominio") === condominioSeleccionado;
    });

    for (var r = 0; r < unidadesFiltradas.length; r++) {
      var nombreTorre = unidadesFiltradas[r].getAttribute("data-torre");
      if (nombreTorre) {
        torresUnicas.add(nombreTorre);
      }
    }

    var arrayTorres = Array.from(torresUnicas).sort();
    for (var s = 0; s < arrayTorres.length; s++) {
      var opcion = document.createElement("option");
      opcion.value = arrayTorres[s];
      opcion.textContent = arrayTorres[s];
      torreGenerar.appendChild(opcion);
    }
  }

  function filtrarUnidadesPorTorre() {
    var condominioSeleccionado = condominioGenerar.value;
    var torreSeleccionada = torreGenerar.value;

    unidadGenerar.innerHTML = '<option value="">Seleccione...</option>';

    if (!torreSeleccionada) {
      unidadGenerar.disabled = true;
      return;
    }

    unidadGenerar.disabled = false;
    var unidadesFiltradas = opcionesOriginales.filter(function (opcion) {
      var coincideCondominio =
        opcion.getAttribute("data-condominio") === condominioSeleccionado;
      var coincideTorre =
        opcion.getAttribute("data-torre") === torreSeleccionada;
      return coincideCondominio && coincideTorre;
    });

    for (var t = 0; t < unidadesFiltradas.length; t++) {
      unidadGenerar.appendChild(unidadesFiltradas[t].cloneNode(true));
    }
  }

  condominioGenerar.addEventListener("change", actualizarTorresYUnidades);
  torreGenerar.addEventListener("change", filtrarUnidadesPorTorre);

  actualizarTorresYUnidades();
}
