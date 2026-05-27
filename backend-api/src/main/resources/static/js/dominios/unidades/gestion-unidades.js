document.addEventListener("DOMContentLoaded", function () {
  if (typeof inicializarAlertasUnidades === "function") {
    inicializarAlertasUnidades();
  }

  if (typeof inicializarTablaUnidades === "function") {
    inicializarTablaUnidades();
  }

  if (typeof inicializarModalUnidades === "function") {
    inicializarModalUnidades();
  }

  if (typeof inicializarEliminarUnidad === "function") {
    inicializarEliminarUnidad();
  }
});
