document.addEventListener("DOMContentLoaded", function () {
  if (typeof inicializarAlertasOcupantes === "function") {
    inicializarAlertasOcupantes();
  }

  if (typeof inicializarAccionesOcupantes === "function") {
    inicializarAccionesOcupantes();
  }
});
