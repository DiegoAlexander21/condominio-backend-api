function inicializarEvidenciasSubidaIncidencia() {
  var formularioIncidencia = document.querySelector("form");
  var botonSeleccionarArchivos = document.getElementById(
    "btnSeleccionarArchivos",
  );
  var urlEvidencia = document.getElementById("urlEvidencia");

  if (!formularioIncidencia || !botonSeleccionarArchivos || !urlEvidencia) {
    return;
  }

  formularioIncidencia.addEventListener("submit", async function (eventoForm) {
    var estado = window.estadoEvidenciasIncidencia || {
      archivosPendientes: [],
    };
    var archivosPendientes = estado.archivosPendientes || [];

    if (archivosPendientes.length === 0) {
      return;
    }

    eventoForm.preventDefault();
    var botonSubir = formularioIncidencia.querySelector(
      'button[type="submit"]',
    );
    var textoOriginal = botonSubir.innerHTML;
    botonSubir.innerHTML = "Subiendo evidencias...";
    botonSubir.disabled = true;

    var nombreNube = botonSeleccionarArchivos.dataset.cloudName;
    var presetSubida = botonSeleccionarArchivos.dataset.uploadPreset;
    var enlaces = [];

    try {
      for (var indice = 0; indice < archivosPendientes.length; indice++) {
        var archivo = archivosPendientes[indice];
        var datosFormulario = new FormData();
        datosFormulario.append("file", archivo);
        datosFormulario.append("upload_preset", presetSubida);

        var respuesta = await fetch(
          "https://api.cloudinary.com/v1_1/" + nombreNube + "/image/upload",
          {
            method: "POST",
            body: datosFormulario,
          },
        );

        if (!respuesta.ok) {
          throw new Error("Error al subir " + archivo.name);
        }

        var datos = await respuesta.json();
        enlaces.push(datos.secure_url);
      }

      urlEvidencia.value = enlaces.join(",");
      formularioIncidencia.submit();
    } catch (errorSubida) {
      console.error("Error subiendo imagenes:", errorSubida);
      alert(
        "Hubo un error subiendo las imagenes. Por favor, intenta de nuevo.",
      );
      botonSubir.innerHTML = textoOriginal;
      botonSubir.disabled = false;
    }
  });
}
