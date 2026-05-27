document.addEventListener("DOMContentLoaded", function () {
  if (typeof inicializarFiltroAreasComunes === "function") {
    inicializarFiltroAreasComunes();
  }

  if (typeof inicializarModalDetallesReserva === "function") {
    inicializarModalDetallesReserva();
  }
});
