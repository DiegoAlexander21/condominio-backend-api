document.addEventListener("DOMContentLoaded", function () {
  var btnPagoTotal = document.getElementById("btnPagoTotal");
  if (btnPagoTotal) {
    btnPagoTotal.addEventListener("click", function () {
      document.getElementById("monto").value = btnPagoTotal.getAttribute("data-saldo");
    });
  }

  var btnSeleccionarArchivos = document.getElementById("btnSeleccionarArchivos");
  var inputArchivos = document.getElementById("evidenciaArchivos");
  var contenedorVistaPrevia = document.getElementById("evidenciaPreview");
  var formPago = document.querySelector(".form-contenedor-margen");
  var urlEvidencia = document.getElementById("urlEvidencia");

  if (btnSeleccionarArchivos && inputArchivos && formPago) {
    var archivosPendientes = [];

    btnSeleccionarArchivos.addEventListener("click", function (evento) {
      evento.preventDefault();
      inputArchivos.click();
    });

    inputArchivos.addEventListener("change", function () {
      var nuevosArchivos = Array.from(this.files);

      if (archivosPendientes.length + nuevosArchivos.length > 5) {
        alert("Solo puedes subir un máximo de 5 imágenes en total.");
        return;
      }

      nuevosArchivos.forEach(function (archivo) {
        if (archivo.size > 10000000) {
          alert("El archivo " + archivo.name + " excede el tamaño máximo de 10MB.");
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

    formPago.addEventListener("submit", async function (eventoForm) {
      var inputMonto = document.getElementById("monto");
      var btnPagoTotalReferencia = document.getElementById("btnPagoTotal");
      var saldoPendiente = btnPagoTotalReferencia ? parseFloat(btnPagoTotalReferencia.getAttribute("data-saldo")) : null;

      if (inputMonto && saldoPendiente !== null) {
        var montoIngresado = parseFloat(inputMonto.value);
        if (isNaN(montoIngresado) || montoIngresado <= 0) {
          alert("El monto ingresado debe ser mayor a cero.");
          eventoForm.preventDefault();
          return;
        }
        if (montoIngresado > saldoPendiente) {
          alert("El monto del pago (S/ " + montoIngresado.toFixed(2) + ") no puede ser mayor al saldo pendiente (S/ " + saldoPendiente.toFixed(2) + ").");
          eventoForm.preventDefault();
          return;
        }
      }

      if (archivosPendientes.length === 0) {
        return;
      }

      eventoForm.preventDefault();
      var btnSubir = formPago.querySelector('button[type="submit"]');
      var textoOriginal = btnSubir.innerHTML;
      btnSubir.innerHTML = "Subiendo comprobantes...";
      btnSubir.disabled = true;

      var nombreNube = btnSeleccionarArchivos.dataset.cloudName;
      var presetSubida = btnSeleccionarArchivos.dataset.uploadPreset;
      var enlaces = [];

      try {
        for (var idx = 0; idx < archivosPendientes.length; idx++) {
          var archivo = archivosPendientes[idx];
          var datosFormulario = new FormData();
          datosFormulario.append("file", archivo);
          datosFormulario.append("upload_preset", presetSubida);

          var respuesta = await fetch("https://api.cloudinary.com/v1_1/" + nombreNube + "/image/upload", {
            method: "POST",
            body: datosFormulario
          });

          if (!respuesta.ok) {
            throw new Error("Error al subir " + archivo.name);
          }

          var datos = await respuesta.json();
          enlaces.push(datos.secure_url);
        }

        urlEvidencia.value = enlaces.join(",");
        formPago.submit();
      } catch (errorSubida) {
        console.error("Error subiendo imágenes:", errorSubida);
        alert("Hubo un error subiendo los comprobantes. Por favor, intenta de nuevo.");
        btnSubir.innerHTML = textoOriginal;
        btnSubir.disabled = false;
      }
    });
  }

  var modalEvidencia = document.getElementById("modalEvidencia");
  var cerrarEvidencia = document.querySelector("#modalEvidencia .cerrar-modal");
  var botonesVerEvidencia = document.querySelectorAll(".boton-evidencia");

  if (modalEvidencia && cerrarEvidencia && botonesVerEvidencia) {
    for (var u = 0; u < botonesVerEvidencia.length; u++) {
      botonesVerEvidencia[u].addEventListener("click", function (eventoEv) {
        eventoEv.preventDefault();
        var urls = this.getAttribute("data-urls");
        var contenedor = document.getElementById("contenedorEvidenciaModal");
        contenedor.innerHTML = "";

        if (urls && urls.trim() !== "") {
          var listaUrls = urls.split(",");
          listaUrls.forEach(function (urlLink) {
            if (urlLink.trim() !== "") {
              var contenedorImagen = document.createElement("div");
              contenedorImagen.style.marginBottom = "15px";
              contenedorImagen.style.textAlign = "center";

              var imgEl = document.createElement("img");
              imgEl.src = urlLink.trim();
              imgEl.style.maxWidth = "100%";
              imgEl.style.borderRadius = "8px";
              imgEl.style.boxShadow = "0 4px 6px rgba(0,0,0,0.1)";
              imgEl.style.cursor = "pointer";
              imgEl.onclick = function () {
                window.open(this.src, "_blank");
              };

              contenedorImagen.appendChild(imgEl);
              contenedor.appendChild(contenedorImagen);
            }
          });
        } else {
          contenedor.innerHTML = "<p>No hay evidencias registradas para este pago.</p>";
        }

        modalEvidencia.classList.add("modal-activo");
      });
    }

    cerrarEvidencia.addEventListener("click", function () {
      modalEvidencia.classList.remove("modal-activo");
    });

    window.addEventListener("click", function (evento) {
      if (evento.target == modalEvidencia) {
        modalEvidencia.classList.remove("modal-activo");
      }
    });
  }
});
