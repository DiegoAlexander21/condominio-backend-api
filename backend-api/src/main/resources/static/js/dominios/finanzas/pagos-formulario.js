function inicializarPagoFormulario() {
  var botonPagoTotal = document.getElementById("btnPagoTotal");
  if (!botonPagoTotal) {
    return;
  }

  botonPagoTotal.addEventListener("click", function () {
    var inputMonto = document.getElementById("monto");
    if (inputMonto) {
      inputMonto.value = botonPagoTotal.getAttribute("data-saldo");
    }
  });
}
