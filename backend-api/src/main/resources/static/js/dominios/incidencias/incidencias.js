document.addEventListener("DOMContentLoaded", function () {
  if (typeof $ !== "undefined") {
    $(".select2").select2({
      theme: "classic",
      width: "100%"
    });
  }

  const selectorCondominio = document.getElementById("condominioId");
  const selectorUnidad = document.getElementById("unidadId");
  const selectorArea = document.getElementById("areaComunId");

  if (selectorCondominio) {
    function actualizarSelectores() {
      const condominioId = selectorCondominio.value;

      if (selectorUnidad) {
        if (condominioId) {
          selectorUnidad.disabled = false;
        } else {
          selectorUnidad.disabled = true;
        }
        Array.from(selectorUnidad.options).forEach(function (opcion) {
          if (opcion.value === "") return;
          if (opcion.dataset.condominio === condominioId || condominioId === "") {
            opcion.style.display = "";
          } else {
            opcion.style.display = "none";
          }
        });
        selectorUnidad.value = "";
      }

      if (selectorArea) {
        if (condominioId) {
          selectorArea.disabled = false;
        } else {
          selectorArea.disabled = true;
        }
        Array.from(selectorArea.options).forEach(function (opcion) {
          if (opcion.value === "") return;
          if (opcion.dataset.condominio === condominioId || condominioId === "") {
            opcion.style.display = "";
          } else {
            opcion.style.display = "none";
          }
        });
        selectorArea.value = "";
      }
    }

    selectorCondominio.addEventListener("change", actualizarSelectores);
    actualizarSelectores();
  }

  const btnSeleccionarArchivos = document.getElementById("btnSeleccionarArchivos");
  const inputArchivos = document.getElementById("evidenciaArchivos");
  const contenedorVistaPrevia = document.getElementById("evidenciaPreview");
  const formularioIncidencia = document.querySelector("form");
  const urlEvidencia = document.getElementById("urlEvidencia");

  if (btnSeleccionarArchivos && inputArchivos && formularioIncidencia) {
    let archivosPendientes = [];

    btnSeleccionarArchivos.addEventListener("click", function (evento) {
      evento.preventDefault();
      inputArchivos.click();
    });

    inputArchivos.addEventListener("change", function () {
      const nuevosArchivos = Array.from(this.files);

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

        const contenedorImagen = document.createElement("div");
        contenedorImagen.className = "contenedor-imagen-previa";

        const imagen = document.createElement("img");
        imagen.src = URL.createObjectURL(archivo);
        imagen.style.cursor = "pointer";
        imagen.onclick = function () {
          window.open(this.src, "_blank");
        };

        const botonEliminar = document.createElement("button");
        botonEliminar.innerHTML = "&times;";
        botonEliminar.className = "boton-eliminar-imagen";
        botonEliminar.onclick = function (eventoEliminar) {
          eventoEliminar.preventDefault();
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

    formularioIncidencia.addEventListener("submit", async function (eventoForm) {
      if (archivosPendientes.length === 0) {
        return;
      }

      eventoForm.preventDefault();
      const botonSubir = formularioIncidencia.querySelector('button[type="submit"]');
      const textoOriginal = botonSubir.innerHTML;
      botonSubir.innerHTML = "Subiendo evidencias...";
      botonSubir.disabled = true;

      const nombreNube = btnSeleccionarArchivos.dataset.cloudName;
      const presetSubida = btnSeleccionarArchivos.dataset.uploadPreset;
      const enlaces = [];

      try {
        for (let idx = 0; idx < archivosPendientes.length; idx++) {
          const archivo = archivosPendientes[idx];
          const datosFormulario = new FormData();
          datosFormulario.append("file", archivo);
          datosFormulario.append("upload_preset", presetSubida);

          const respuesta = await fetch("https://api.cloudinary.com/v1_1/" + nombreNube + "/image/upload", {
            method: "POST",
            body: datosFormulario
          });

          if (!respuesta.ok) {
            throw new Error("Error al subir " + archivo.name);
          }

          const datos = await respuesta.json();
          enlaces.push(datos.secure_url);
        }

        urlEvidencia.value = enlaces.join(",");
        formularioIncidencia.submit();
      } catch (errorSubida) {
        console.error("Error subiendo imágenes:", errorSubida);
        alert("Hubo un error subiendo las imágenes. Por favor, intenta de nuevo.");
        botonSubir.innerHTML = textoOriginal;
        botonSubir.disabled = false;
      }
    });
  }

  const modalIncidencia = document.getElementById("modalIncidencia");
  const botonesDetalles = document.querySelectorAll(".boton-detalles");
  const cerrarModal = document.querySelector(".cerrar-modal");

  if (modalIncidencia && botonesDetalles) {
    botonesDetalles.forEach(function (boton) {
      boton.addEventListener("click", async function () {
        const id = this.dataset.id;
        document.getElementById("modalCausa").textContent = boton.dataset.causa || "Sin causa";
        document.getElementById("modalLugar").textContent = boton.dataset.lugar || "No especificada";
        document.getElementById("modalEstado").textContent = boton.dataset.estado || "No especificado";
        document.getElementById("modalGravedad").textContent = this.dataset.gravedad;
        document.getElementById("modalFecha").textContent = this.dataset.fecha;
        document.getElementById("modalResponsable").textContent =
          this.dataset.responsable && this.dataset.responsable.trim() !== "" ? this.dataset.responsable : "Sin asignar";
        document.getElementById("modalDesc").textContent = boton.dataset.desc || "Sin descripción";

        const contenedorEvidencias = document.getElementById("modalEvidencias");
        contenedorEvidencias.innerHTML = "<p>Cargando evidencias...</p>";
        modalIncidencia.classList.add("modal-activo");

        try {
          const respuestaUrls = await fetch("/incidencias/evidencias/" + id);
          if (!respuestaUrls.ok) throw new Error("Error de red");
          const urlsEvidencias = await respuestaUrls.json();

          contenedorEvidencias.innerHTML = "";
          if (urlsEvidencias.length === 0) {
            contenedorEvidencias.innerHTML = "<p>No hay evidencias registradas.</p>";
          } else {
            urlsEvidencias.forEach(function (urlLink) {
              const contenedor = document.createElement("div");
              contenedor.className = "contenedor-imagen-previa";
              const imagenEvidencia = document.createElement("img");
              imagenEvidencia.src = urlLink;
              imagenEvidencia.style.cursor = "pointer";
              imagenEvidencia.onclick = function () {
                window.open(this.src, "_blank");
              };
              contenedor.appendChild(imagenEvidencia);
              contenedorEvidencias.appendChild(contenedor);
            });
          }
        } catch (errorCarga) {
          console.error("Error al cargar evidencias:", errorCarga);
          contenedorEvidencias.innerHTML = "<p>Error al cargar las evidencias.</p>";
        }
      });
    });

    if (cerrarModal) {
      cerrarModal.addEventListener("click", function () {
        modalIncidencia.classList.remove("modal-activo");
      });
    }

    window.addEventListener("click", function (evento) {
      if (evento.target == modalIncidencia) {
        modalIncidencia.classList.remove("modal-activo");
      }
    });
  }
});
