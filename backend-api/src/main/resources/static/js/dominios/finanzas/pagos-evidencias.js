function inicializarEvidenciasPago() {
  var botonSeleccionarArchivos = document.getElementById(
    "btnSeleccionarArchivos",
  );
  var inputArchivos = document.getElementById("evidenciaArchivos");
  var contenedorVistaPrevia = document.getElementById("evidenciaPreview");
  var formularioPago = document.querySelector(".form-contenedor-margen");
  var urlEvidencia = document.getElementById("urlEvidencia");

  if (!botonSeleccionarArchivos || !inputArchivos || !formularioPago) {
    return;
  }

  var archivosPendientes = [];

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
      botonEliminar.onclick = function (eventoBoton) {
        eventoBoton.preventDefault();
        contenedorImagen.remove();
        archivosPendientes = archivosPendientes.filter(function (f) {
          return f !== archivo;
        });
      };

      contenedorImagen.appendChild(imagen);
      contenedorImagen.appendChild(botonEliminar);
      contenedorVistaPrevia.appendChild(contenedorImagen);
    });

    this.value = "";
  });

  formularioPago.addEventListener("submit", async function (eventoForm) {
    var inputMonto = document.getElementById("monto");
    var botonPagoTotal = document.getElementById("btnPagoTotal");
    var saldoPendiente = botonPagoTotal
      ? parseFloat(botonPagoTotal.getAttribute("data-saldo"))
      : null;

    if (inputMonto && saldoPendiente !== null) {
      var montoIngresado = parseFloat(inputMonto.value);
      if (isNaN(montoIngresado) || montoIngresado <= 0) {
        alert("El monto ingresado debe ser mayor a cero.");
        eventoForm.preventDefault();
        return;
      }
      if (montoIngresado > saldoPendiente) {
        alert(
          "El monto del pago (S/ " +
            montoIngresado.toFixed(2) +
            ") no puede ser mayor al saldo pendiente (S/ " +
            saldoPendiente.toFixed(2) +
            ").",
        );
        eventoForm.preventDefault();
        return;
      }
    }

    if (archivosPendientes.length === 0) {
      return;
    }

    eventoForm.preventDefault();
    var botonSubir = formularioPago.querySelector('button[type="submit"]');
    var textoOriginal = botonSubir.innerHTML;
    botonSubir.innerHTML = "Subiendo comprobantes...";
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

      if (urlEvidencia) {
        urlEvidencia.value = enlaces.join(",");
      }
      formularioPago.submit();
    } catch (errorSubida) {
      console.error("Error subiendo imagenes:", errorSubida);
      alert(
        "Hubo un error subiendo los comprobantes. Por favor, intenta de nuevo.",
      );
      botonSubir.innerHTML = textoOriginal;
      botonSubir.disabled = false;
    }
  });
}
