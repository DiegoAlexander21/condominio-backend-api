function inicializarAlertasUnidades() {
  var alertas = document.querySelectorAll(".alerta-exito, .alerta-error");

  for (var i = 0; i < alertas.length; i++) {
    setTimeout(
      function (elemento) {
        elemento.classList.add("oculto");
      },
      5000,
      alertas[i],
    );
  }
}
