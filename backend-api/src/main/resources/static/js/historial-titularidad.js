document.addEventListener("DOMContentLoaded", function () {
  var formulario = document.getElementById("formularioHistorial");
  var departamentoId = document.getElementById("departamentoId");
  var propietarioAnterior = document.getElementById("propietarioAnterior");
  var nuevoPropietario = document.getElementById("nuevoPropietario");
  var alertas = document.querySelectorAll(".alerta-exito");

  for (var i = 0; i < alertas.length; i++) {
    setTimeout(function (elemento) {
      elemento.style.display = "none";
    }, 5000, alertas[i]);
  }

  if (!formulario) {
    return;
  }

  var cuerpo = document.getElementById("cuerpoTabla");

  if (cuerpo) {
    var filasReales = cuerpo.querySelectorAll("tr.fila-dato");

    for (var k = 0; k < filasReales.length; k++) {
      filasReales[k].addEventListener("click", function () {
        for (var m = 0; m < filasReales.length; m++) {
          filasReales[m].style.background = "";
        }
        this.style.background = "#e8e6df";
      });
    }
  }

  departamentoId.addEventListener("focus", function () {
    if (this.value === "0") {
      this.value = "";
    }
  });

  departamentoId.addEventListener("input", function () {
    this.value = this.value.replace(/[^\d]/g, "");
    limpiarError(this);
  });

  propietarioAnterior.addEventListener("blur", function () {
    if (propietarioAnterior.value !== "") {
      validarTexto(propietarioAnterior);
    }
  });

  nuevoPropietario.addEventListener("blur", function () {
    if (nuevoPropietario.value !== "") {
      validarTexto(nuevoPropietario);
    }
  });

  formulario.addEventListener("submit", function (evento) {
    var deptoValido = validarNumero(departamentoId);
    if (!deptoValido) { evento.preventDefault(); return; }

    var anteriorValido = validarTexto(propietarioAnterior);
    if (!anteriorValido) { evento.preventDefault(); return; }

    var nuevoValido = validarTexto(nuevoPropietario);
    if (!nuevoValido) { evento.preventDefault(); return; }

    if (propietarioAnterior.value.trim().toLowerCase() === nuevoPropietario.value.trim().toLowerCase()) {
      mostrarError(nuevoPropietario, "El nuevo dueño no puede ser la misma persona que el anterior.");
      evento.preventDefault();
    }
  });
});