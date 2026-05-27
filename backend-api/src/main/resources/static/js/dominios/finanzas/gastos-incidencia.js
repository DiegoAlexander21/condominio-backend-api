function inicializarIncidenciaGasto() {
  var selectTipoGasto = document.getElementById("tipoGasto");
  var grupoIncidencia = document.getElementById("grupoIncidencia");

  if (!selectTipoGasto || !grupoIncidencia) {
    return;
  }

  if (selectTipoGasto.value === "EXTRAORDINARIO") {
    grupoIncidencia.classList.remove("oculto");
  } else {
    grupoIncidencia.classList.add("oculto");
  }

  selectTipoGasto.addEventListener("change", function () {
    if (this.value === "EXTRAORDINARIO") {
      grupoIncidencia.classList.remove("oculto");
    } else {
      grupoIncidencia.classList.add("oculto");
      var inputIncidencia = document.getElementById("incidenciaId");
      if (inputIncidencia) {
        inputIncidencia.value = "";
      }
    }
  });
}
