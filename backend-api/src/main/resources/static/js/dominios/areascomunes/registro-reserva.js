document.addEventListener("DOMContentLoaded", function () {
  if (typeof inicializarFiltrosReserva === "function") {
    inicializarFiltrosReserva();
  }

  if (typeof inicializarHorariosReserva === "function") {
    inicializarHorariosReserva();
  }
});
