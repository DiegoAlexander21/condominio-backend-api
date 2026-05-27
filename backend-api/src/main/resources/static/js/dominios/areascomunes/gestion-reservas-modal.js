function inicializarModalDetallesReserva() {
  const botonesDetallesUnidad = document.querySelectorAll(".boton-detalles");
  const modal = document.getElementById("modalDetalles");
  const botonCerrar = document.querySelector(".cerrar-modal");

  if (!botonesDetallesUnidad || !modal || !botonCerrar) {
    return;
  }

  botonesDetallesUnidad.forEach((boton) => {
    boton.addEventListener("click", function (e) {
      e.preventDefault();

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

      const residente = this.getAttribute("data-residente");
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

      modal.style.display = "block";
    });
  });

  botonCerrar.addEventListener("click", function () {
    modal.style.display = "none";
  });

  window.addEventListener("click", function (event) {
    if (event.target === modal) {
      modal.style.display = "none";
    }
  });
}
