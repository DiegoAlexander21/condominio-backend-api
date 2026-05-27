function inicializarModalDistribucion() {
  var modalDistribucion = document.getElementById("modalDistribucion");
  var botonesDistribuir = document.querySelectorAll(".boton-distribuir");
  var cerrarModal = document.querySelector(".cerrar-modal");

  if (!modalDistribucion || !botonesDistribuir.length) {
    return;
  }

  for (var i = 0; i < botonesDistribuir.length; i++) {
    botonesDistribuir[i].addEventListener("click", function () {
      var id = this.dataset.id;
      var descripcion = this.dataset.desc;
      var metodo = this.dataset.metodo;
      var monto = this.dataset.monto;
      var metodoCrudo = this.dataset.metodoRaw;
      var unidadCausante = this.dataset.unidad;
      var tipoGasto = this.dataset.tipo;

      document.getElementById("gastoId").value = id;
      document.getElementById("tipoGastoRedirect").value = tipoGasto;
      document.getElementById("descGastoModal").innerHTML =
        "Se va a distribuir: <strong>" +
        descripcion +
        "</strong> (S/ " +
        monto +
        ")<br>Metodo: <strong>" +
        metodo +
        "</strong>";

      var grupoUnidad = document.getElementById("grupoUnidad");
      var inputUnidad = document.getElementById("unidadId");

      if (metodoCrudo === "COBRO_DIRECTO") {
        grupoUnidad.classList.remove("oculto");
        inputUnidad.required = true;
        if (unidadCausante && unidadCausante !== "null") {
          inputUnidad.value = unidadCausante;
          inputUnidad.readOnly = true;
        } else {
          inputUnidad.value = "";
          inputUnidad.readOnly = false;
        }
      } else {
        grupoUnidad.classList.add("oculto");
        inputUnidad.required = false;
        inputUnidad.value = "";
        inputUnidad.readOnly = false;
      }

      modalDistribucion.classList.add("modal-activo");
    });
  }

  if (cerrarModal) {
    cerrarModal.addEventListener("click", function () {
      modalDistribucion.classList.remove("modal-activo");
    });
  }

  window.addEventListener("click", function (evento) {
    if (evento.target == modalDistribucion) {
      modalDistribucion.classList.remove("modal-activo");
    }
  });
}
