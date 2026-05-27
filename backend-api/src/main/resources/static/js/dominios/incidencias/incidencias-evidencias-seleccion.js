function inicializarEvidenciasSeleccionIncidencia() {
  var botonSeleccionarArchivos = document.getElementById(
    "btnSeleccionarArchivos",
  );
  var inputArchivos = document.getElementById("evidenciaArchivos");
  var contenedorVistaPrevia = document.getElementById("evidenciaPreview");

  if (!botonSeleccionarArchivos || !inputArchivos || !contenedorVistaPrevia) {
    return;
  }

  var archivosPendientes = [];
  window.estadoEvidenciasIncidencia = {
    archivosPendientes: archivosPendientes,
  };

  botonSeleccionarArchivos.addEventListener("click", function (evento) {
    evento.preventDefault();
    inputArchivos.click();
  });

  inputArchivos.addEventListener("change", function () {
    var nuevosArchivos = Array.from(this.files);

    if (archivosPendientes.length + nuevosArchivos.length > 5) {
      alert("Solo puedes subir un maximo de 5 imagenes en total.");
      return;
    }

    nuevosArchivos.forEach(function (archivo) {
      if (archivo.size > 10000000) {
        alert(
          "El archivo " + archivo.name + " excede el tamano maximo de 10MB.",
        );
        return;
      }

      archivosPendientes.push(archivo);

      var contenedorImagen = document.createElement("div");
      contenedorImagen.className = "contenedor-imagen-previa";

      var imagen = document.createElement("img");
      imagen.src = URL.createObjectURL(archivo);
      imagen.style.cursor = "pointer";
      imagen.onclick = function () {
        window.open(this.src, "_blank");
      };

      var botonEliminar = document.createElement("button");
      botonEliminar.innerHTML = "&times;";
      botonEliminar.className = "boton-eliminar-imagen";
      botonEliminar.onclick = function (eventoEliminar) {
        eventoEliminar.preventDefault();
        contenedorImagen.remove();
        archivosPendientes = archivosPendientes.filter(function (f) {
          return f !== archivo;
        });
        window.estadoEvidenciasIncidencia.archivosPendientes =
          archivosPendientes;
      };

      contenedorImagen.appendChild(imagen);
      contenedorImagen.appendChild(botonEliminar);
      contenedorVistaPrevia.appendChild(contenedorImagen);
    });

    this.value = "";
  });
}
