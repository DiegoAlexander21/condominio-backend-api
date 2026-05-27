function inicializarTablaUnidades() {
  var cuerpo = document.getElementById("cuerpoTabla");

  if (!cuerpo) {
    return;
  }

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
