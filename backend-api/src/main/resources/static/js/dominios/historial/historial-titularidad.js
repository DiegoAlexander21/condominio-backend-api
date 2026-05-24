document.addEventListener("DOMContentLoaded", function () {
  var alertas = document.querySelectorAll(".alerta-exito");

  for (var i = 0; i < alertas.length; i++) {
    setTimeout(function (elemento) {
      elemento.style.display = "none";
    }, 5000, alertas[i]);
  }

  var cuerpo = document.getElementById("cuerpoTabla");

  if (cuerpo) {
    var filasReales = cuerpo.querySelectorAll("tr.fila-dato");

    for (var k = 0; k < filasReales.length; k++) {
      filasReales[k].addEventListener("click", function () {
        for (var m = 0; m < filasReales.length; m++) {
          filasReales[m].style.background = "";
        }
        this.style.background = "#e8e6df";
      });
    }
  }
});