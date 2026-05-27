function inicializarEliminarUnidad() {
  var botonesEliminar = document.querySelectorAll(".btn-eliminar");
  for (var j = 0; j < botonesEliminar.length; j++) {
    botonesEliminar[j].addEventListener("click", function (evento) {
      var confirmacion = confirm(
        "¿Seguro que desea eliminar esta unidad? Todo su historial sera eliminado (Cascada).",
      );
      if (!confirmacion) {
        evento.preventDefault();
      }
    });
  }
}
