document.addEventListener("DOMContentLoaded", function () {
  if (typeof inicializarDatosReportes === "function") {
    inicializarDatosReportes();
  }

  if (typeof inicializarGraficoIncidencias === "function") {
    inicializarGraficoIncidencias();
  }

  if (typeof inicializarGraficoGastos === "function") {
    inicializarGraficoGastos();
  }

  if (typeof inicializarGraficoMorosas === "function") {
    inicializarGraficoMorosas();
  }
});
