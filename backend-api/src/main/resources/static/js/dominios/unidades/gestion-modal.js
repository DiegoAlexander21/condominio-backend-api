function inicializarModalUnidades() {
  var botonesDetalles = document.querySelectorAll(".boton-detalles");
  var modal = document.getElementById("modalDetalles");
  var botonCerrar = document.querySelector(".cerrar-modal");

  if (!botonesDetalles.length || !modal || !botonCerrar) {
    return;
  }

  for (var n = 0; n < botonesDetalles.length; n++) {
    botonesDetalles[n].addEventListener("click", function (evento) {
      evento.stopPropagation();
      document.getElementById("det-condominio").textContent =
        this.getAttribute("data-condominio");
      document.getElementById("det-unidad").textContent =
        this.getAttribute("data-unidad");
      document.getElementById("det-torre").textContent =
        this.getAttribute("data-torre");
      document.getElementById("det-piso").textContent =
        this.getAttribute("data-piso");
      document.getElementById("det-area").textContent =
        this.getAttribute("data-area");
      document.getElementById("det-estado").textContent =
        this.getAttribute("data-estado");
      document.getElementById("det-propietario").textContent =
        this.getAttribute("data-propietario") || "Sin registrar";
      document.getElementById("det-dni-prop").textContent =
        this.getAttribute("data-dni-prop") || "-";
      document.getElementById("det-tel-prop").textContent =
        this.getAttribute("data-tel-prop") || "-";
      document.getElementById("det-email-prop").textContent =
        this.getAttribute("data-email-prop") || "-";

      var residente = this.getAttribute("data-residente");
      if (!residente || residente === "null" || residente.trim() === "") {
        document.getElementById("det-residente").textContent =
          "Sin residente registrado";
        document.getElementById("det-dni-res").textContent = "-";
        document.getElementById("det-tel-res").textContent = "-";
        document.getElementById("det-parentesco").textContent = "-";
      } else {
        document.getElementById("det-residente").textContent = residente;
        document.getElementById("det-dni-res").textContent =
          this.getAttribute("data-dni-res") || "-";
        document.getElementById("det-tel-res").textContent =
          this.getAttribute("data-tel-res") || "-";
        document.getElementById("det-parentesco").textContent =
          this.getAttribute("data-parentesco") || "-";
      }

      modal.classList.add("modal-activo");
    });
  }

  botonCerrar.addEventListener("click", function () {
    modal.classList.remove("modal-activo");
  });
}
