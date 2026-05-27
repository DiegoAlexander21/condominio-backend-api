function inicializarDatosReportes() {
  var elementosIncidencias = document.querySelectorAll(".datos-incidencia");
  var incidenciasNombres = [];
  var incidenciasTotales = [];

  elementosIncidencias.forEach(function (elemento) {
    incidenciasNombres.push(elemento.getAttribute("data-area"));
    incidenciasTotales.push(parseInt(elemento.getAttribute("data-total")) || 0);
  });

  var elementosGastos = document.querySelectorAll(".datos-gasto");
  var gastosNombres = [];
  var gastosMontos = [];

  elementosGastos.forEach(function (elemento) {
    gastosNombres.push(elemento.getAttribute("data-area"));
    gastosMontos.push(parseFloat(elemento.getAttribute("data-monto")) || 0.0);
  });

  var elementosMorosas = document.querySelectorAll(".datos-morosa");
  var morosasNombres = [];
  var morosasSaldos = [];

  elementosMorosas.forEach(function (elemento) {
    morosasNombres.push(elemento.getAttribute("data-unidad"));
    morosasSaldos.push(parseFloat(elemento.getAttribute("data-saldo")) || 0.0);
  });

  window.datosReportes = {
    incidenciasNombres: incidenciasNombres,
    incidenciasTotales: incidenciasTotales,
    gastosNombres: gastosNombres,
    gastosMontos: gastosMontos,
    morosasNombres: morosasNombres,
    morosasSaldos: morosasSaldos,
  };
}
