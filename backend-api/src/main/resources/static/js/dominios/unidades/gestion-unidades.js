document.addEventListener("DOMContentLoaded", function () {
  var cuerpo = document.getElementById("cuerpoTabla");
  var alertas = document.querySelectorAll(".alerta-exito");

  for (var i = 0; i < alertas.length; i++) {
    setTimeout(function (elemento) {
      elemento.style.display = "none";
    }, 5000, alertas[i]);
  }

  if (!cuerpo) {
    return;
  }

  var filasReales = cuerpo.querySelectorAll("tr.fila-dato");

    for (var k = 0; k < filasReales.length; k++) {
      filasReales[k].addEventListener("click", function () {
        for (var m = 0; m < filasReales.length; m++) {
          filasReales[m].style.background = "";
        }
        this.style.background = "#e8e6df";
      });
    }

  const botonesDetalles = document.querySelectorAll(".boton-detalles")
  const modal = document.getElementById("modalDetalles")
  const botonCerrar = document.querySelector(".cerrar-modal")

  if (botonesDetalles && modal && botonCerrar) {
      botonesDetalles.forEach(boton => {
          boton.addEventListener("click", function(e) {
              e.stopPropagation(); // Evitar que seleccione la fila
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
              
              let residente = this.getAttribute("data-residente");
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

              modal.style.display = "block";
          });
      });

      botonCerrar.addEventListener("click", function() {
          modal.style.display = "none";
      });
  }
});