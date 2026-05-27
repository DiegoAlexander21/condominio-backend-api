document.addEventListener("DOMContentLoaded", function () {
  if (typeof inicializarModalDistribucion === "function") {
    inicializarModalDistribucion();
  }

  if (typeof inicializarIncidenciaGasto === "function") {
    inicializarIncidenciaGasto();
  }

  if (typeof inicializarPeriodoGasto === "function") {
    inicializarPeriodoGasto();
  }

  if (typeof inicializarModalDetallesGasto === "function") {
    inicializarModalDetallesGasto();
  }

  if (typeof inicializarFiltroTorreGasto === "function") {
    inicializarFiltroTorreGasto();
  }

  if (typeof inicializarFiltroIncidenciaGasto === "function") {
    inicializarFiltroIncidenciaGasto();
  }

  if (typeof inicializarGeneracionEstadoCuenta === "function") {
    inicializarGeneracionEstadoCuenta();
  }
});
