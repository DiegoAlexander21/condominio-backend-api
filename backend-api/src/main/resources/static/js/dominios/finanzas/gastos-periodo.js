function inicializarPeriodoGasto() {
  var inputPeriodo = document.getElementById("periodo");
  if (!inputPeriodo || inputPeriodo.value) {
    return;
  }

  var hoy = new Date();
  var anio = hoy.getFullYear();
  var mes = String(hoy.getMonth() + 1).padStart(2, "0");
  inputPeriodo.value = anio + "-" + mes;
}
