document.addEventListener("DOMContentLoaded", function () {
  var cuerpo = document.getElementById("cuerpoTabla");
  var alertas = document.querySelectorAll(".alerta-exito, .alerta-error");

  for (var i = 0; i < alertas.length; i++) {
    setTimeout(function (elemento) {
      elemento.classList.add("oculto");
    }, 5000, alertas[i]);
  }

  if (cuerpo) {
    var filasReales = cuerpo.querySelectorAll("tr.fila-dato");
    for (var k = 0; k < filasReales.length; k++) {
      filasReales[k].addEventListener("click", function () {
        for (var m = 0; m < filasReales.length; m++) {
          filasReales[m].classList.remove("fila-seleccionada");
        }
        this.classList.add("fila-seleccionada");
      });
    }
  }

  var botonesDetalles = document.querySelectorAll(".boton-detalles");
  var modal = document.getElementById("modalDetalles");
  var botonCerrar = document.querySelector(".cerrar-modal");

  if (botonesDetalles && modal && botonCerrar) {
    for (var n = 0; n < botonesDetalles.length; n++) {
      botonesDetalles[n].addEventListener("click", function (evento) {
        evento.stopPropagation();
        document.getElementById("det-condominio").textContent = this.getAttribute("data-condominio");
        document.getElementById("det-unidad").textContent = this.getAttribute("data-unidad");
        document.getElementById("det-torre").textContent = this.getAttribute("data-torre");
        document.getElementById("det-piso").textContent = this.getAttribute("data-piso");
        document.getElementById("det-area").textContent = this.getAttribute("data-area");
        document.getElementById("det-estado").textContent = this.getAttribute("data-estado");
        document.getElementById("det-propietario").textContent = this.getAttribute("data-propietario") || "Sin registrar";
        document.getElementById("det-dni-prop").textContent = this.getAttribute("data-dni-prop") || "-";
        document.getElementById("det-tel-prop").textContent = this.getAttribute("data-tel-prop") || "-";
        document.getElementById("det-email-prop").textContent = this.getAttribute("data-email-prop") || "-";

        var residente = this.getAttribute("data-residente");
        if (!residente || residente === "null" || residente.trim() === "") {
          document.getElementById("det-residente").textContent = "Sin residente registrado";
          document.getElementById("det-dni-res").textContent = "-";
          document.getElementById("det-tel-res").textContent = "-";
          document.getElementById("det-parentesco").textContent = "-";
        } else {
          document.getElementById("det-residente").textContent = residente;
          document.getElementById("det-dni-res").textContent = this.getAttribute("data-dni-res") || "-";
          document.getElementById("det-tel-res").textContent = this.getAttribute("data-tel-res") || "-";
          document.getElementById("det-parentesco").textContent = this.getAttribute("data-parentesco") || "-";
        }

        modal.classList.add("modal-activo");
      });
    }

    botonCerrar.addEventListener("click", function () {
      modal.classList.remove("modal-activo");
    });
  }

  var botonesEliminar = document.querySelectorAll(".btn-eliminar");
  for (var j = 0; j < botonesEliminar.length; j++) {
    botonesEliminar[j].addEventListener("click", function (evento) {
      var confirmacion = confirm("¿Seguro que desea eliminar esta unidad? Todo su historial será eliminado (Cascada).");
      if (!confirmacion) {
        evento.preventDefault();
      }
    });
  }
});