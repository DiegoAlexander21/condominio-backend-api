document.addEventListener("DOMContentLoaded", function () {
  var alertas = document.querySelectorAll(".alerta-exito, .alerta-error");

  for (var i = 0; i < alertas.length; i++) {
    setTimeout(function (elemento) {
      elemento.classList.add("oculto");
    }, 5000, alertas[i]);
  }

  var cuerpo = document.getElementById("cuerpoTabla");

  if (cuerpo) {
    var filasReales = cuerpo.querySelectorAll("tr.fila-dato");

    for (var k = 0; k < filasReales.length; k++) {
      filasReales[k].addEventListener("click", function () {
        for (var m = 0; m < filasReales.length; m++) {
          filasReales[m].classList.remove("fila-seleccionada");
        }
        this.classList.add("fila-seleccionada");
      });
    }
  }
});