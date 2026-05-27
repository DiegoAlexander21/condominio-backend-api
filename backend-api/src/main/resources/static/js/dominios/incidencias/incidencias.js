document.addEventListener("DOMContentLoaded", function () {
  if (typeof inicializarSelectoresIncidencia === "function") {
    inicializarSelectoresIncidencia();
  }

  if (typeof inicializarEvidenciasSeleccionIncidencia === "function") {
    inicializarEvidenciasSeleccionIncidencia();
  }

  if (typeof inicializarEvidenciasSubidaIncidencia === "function") {
    inicializarEvidenciasSubidaIncidencia();
  }

  if (typeof inicializarModalIncidencia === "function") {
    inicializarModalIncidencia();
  }
});
