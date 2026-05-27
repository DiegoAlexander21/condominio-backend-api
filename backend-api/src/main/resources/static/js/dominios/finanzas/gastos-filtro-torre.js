function inicializarFiltroTorreGasto() {
  var condominioSelect = document.getElementById("condominioId");
  var torreSelect = document.getElementById("torre");

  if (!condominioSelect || !torreSelect) {
    return;
  }

  var opcionesTorre = Array.from(torreSelect.options).map(function (opcion) {
    return opcion.cloneNode(true);
  });

  function filtrarTorres(preservarValor) {
    var condominioSeleccionado = condominioSelect.value;
    var torreActual = torreSelect.value;
    torreSelect.innerHTML = "";

    for (var p = 0; p < opcionesTorre.length; p++) {
      var opcion = opcionesTorre[p];
      if (opcion.value === "") {
        torreSelect.appendChild(opcion.cloneNode(true));
      } else if (
        opcion.getAttribute("data-condominio") === condominioSeleccionado
      ) {
        torreSelect.appendChild(opcion.cloneNode(true));
      }
    }

    if (preservarValor) {
      var existeOpcion = Array.from(torreSelect.options).some(function (o) {
        return o.value === torreActual;
      });
      torreSelect.value = existeOpcion ? torreActual : "";
    } else {
      torreSelect.value = "";
    }
  }

  condominioSelect.addEventListener("change", function () {
    filtrarTorres(false);
  });

  filtrarTorres(true);
}
