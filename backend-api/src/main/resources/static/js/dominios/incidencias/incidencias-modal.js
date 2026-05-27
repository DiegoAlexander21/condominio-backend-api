function inicializarModalIncidencia() {
  var modalIncidencia = document.getElementById("modalIncidencia");
  var botonesDetalles = document.querySelectorAll(".boton-detalles");
  var cerrarModal = document.querySelector(".cerrar-modal");

  if (!modalIncidencia || !botonesDetalles.length) {
    return;
  }

  botonesDetalles.forEach(function (boton) {
    boton.addEventListener("click", async function () {
      var idIncidencia = this.dataset.id;
      document.getElementById("modalCausa").textContent =
        boton.dataset.causa || "Sin causa";
      document.getElementById("modalLugar").textContent =
        boton.dataset.lugar || "No especificada";
      document.getElementById("modalEstado").textContent =
        boton.dataset.estado || "No especificado";
      document.getElementById("modalGravedad").textContent =
        this.dataset.gravedad;
      document.getElementById("modalFecha").textContent = this.dataset.fecha;
      document.getElementById("modalResponsable").textContent =
        this.dataset.responsable && this.dataset.responsable.trim() !== ""
          ? this.dataset.responsable
          : "Sin asignar";
      document.getElementById("modalDesc").textContent =
        boton.dataset.desc || "Sin descripcion";

      var contenedorEvidencias = document.getElementById("modalEvidencias");
      contenedorEvidencias.innerHTML = "<p>Cargando evidencias...</p>";
      modalIncidencia.classList.add("modal-activo");

      try {
        var respuestaUrls = await fetch(
          "/incidencias/evidencias/" + idIncidencia,
        );
        if (!respuestaUrls.ok) throw new Error("Error de red");
        var enlacesEvidencias = await respuestaUrls.json();

        contenedorEvidencias.innerHTML = "";
        if (enlacesEvidencias.length === 0) {
          contenedorEvidencias.innerHTML =
            "<p>No hay evidencias registradas.</p>";
        } else {
          enlacesEvidencias.forEach(function (enlace) {
            var contenedor = document.createElement("div");
            contenedor.className = "contenedor-imagen-previa";
            var imagenEvidencia = document.createElement("img");
            imagenEvidencia.src = enlace;
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
        contenedorEvidencias.innerHTML =
          "<p>Error al cargar las evidencias.</p>";
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
