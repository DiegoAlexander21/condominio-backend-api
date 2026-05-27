function inicializarFiltroIncidenciaGasto() {
  var condominioSelect = document.getElementById("condominioId");
  var torreSelect = document.getElementById("torre");
  var incidenciaSelect = document.getElementById("incidenciaId");

  if (!condominioSelect || !incidenciaSelect) {
    return;
  }

  var opcionesIncidencia = Array.from(incidenciaSelect.options).map(
    function (opcion) {
      return opcion.cloneNode(true);
    },
  );

  function filtrarIncidencias(preservarValor) {
    var condominioSeleccionado = condominioSelect.value;
    var torreActual = torreSelect ? torreSelect.value : "";
    var incidenciaActual = incidenciaSelect.value;
    incidenciaSelect.innerHTML = "";

    for (var q = 0; q < opcionesIncidencia.length; q++) {
      var opcion = opcionesIncidencia[q];
      if (opcion.value === "") {
        incidenciaSelect.appendChild(opcion.cloneNode(true));
      } else if (
        opcion.getAttribute("data-condominio") === condominioSeleccionado
      ) {
        var torreIncidencia = opcion.getAttribute("data-torre");
        if (
          !torreActual ||
          torreIncidencia === "" ||
          torreIncidencia === torreActual
        ) {
          incidenciaSelect.appendChild(opcion.cloneNode(true));
        }
      }
    }

    if (preservarValor) {
      var existeOpcion = Array.from(incidenciaSelect.options).some(
        function (o) {
          return o.value === incidenciaActual;
        },
      );
      incidenciaSelect.value = existeOpcion ? incidenciaActual : "";
    } else {
      incidenciaSelect.value = "";
    }
  }

  condominioSelect.addEventListener("change", function () {
    filtrarIncidencias(false);
  });

  if (torreSelect) {
    torreSelect.addEventListener("change", function () {
      filtrarIncidencias(true);
    });
  }

  filtrarIncidencias(true);
}
