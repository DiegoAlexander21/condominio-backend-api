function inicializarSelectoresIncidencia() {
  if (typeof $ !== "undefined") {
    $(".select2").select2({
      theme: "classic",
      width: "100%",
    });
  }

  var selectorCondominio = document.getElementById("condominioId");
  var selectorUnidad = document.getElementById("unidadId");
  var selectorArea = document.getElementById("areaComunId");

  if (!selectorCondominio) {
    return;
  }

  function actualizarSelectores() {
    var condominioSeleccionado = selectorCondominio.value;

    if (selectorUnidad) {
      selectorUnidad.disabled = !condominioSeleccionado;
      Array.from(selectorUnidad.options).forEach(function (opcion) {
        if (opcion.value === "") return;
        opcion.style.display =
          opcion.dataset.condominio === condominioSeleccionado ||
          condominioSeleccionado === ""
            ? ""
            : "none";
      });
      selectorUnidad.value = "";
    }

    if (selectorArea) {
      selectorArea.disabled = !condominioSeleccionado;
      Array.from(selectorArea.options).forEach(function (opcion) {
        if (opcion.value === "") return;
        opcion.style.display =
          opcion.dataset.condominio === condominioSeleccionado ||
          condominioSeleccionado === ""
            ? ""
            : "none";
      });
      selectorArea.value = "";
    }
  }

  selectorCondominio.addEventListener("change", actualizarSelectores);
  actualizarSelectores();
}
