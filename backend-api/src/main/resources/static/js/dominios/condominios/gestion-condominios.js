document.addEventListener("DOMContentLoaded", function () {
  var cuerpo = document.getElementById("cuerpoTabla");
  var alertas = document.querySelectorAll(".alerta-exito, .alerta-error");

  for (var i = 0; i < alertas.length; i++) {
    setTimeout(function (elemento) {
      elemento.classList.add("oculto");
    }, 5000, alertas[i]);
  }

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

  var botonesEliminar = document.querySelectorAll(".btn-eliminar");
  for (var j = 0; j < botonesEliminar.length; j++) {
    botonesEliminar[j].addEventListener("click", function (evento) {
      var confirmacion = confirm("¿Seguro que desea eliminar este condominio? Todas las unidades, propietarios y residentes registrados en él serán eliminados (Cascada).");
      if (!confirmacion) {
        evento.preventDefault();
      }
    });
  }
});
