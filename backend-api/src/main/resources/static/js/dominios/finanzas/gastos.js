document.addEventListener("DOMContentLoaded", function () {
  var modalDistribucion = document.getElementById("modalDistribucion");
  var botonesDistribuir = document.querySelectorAll(".boton-distribuir");
  var cerrarModal = document.querySelector(".cerrar-modal");

  if (modalDistribucion && botonesDistribuir) {
    for (var i = 0; i < botonesDistribuir.length; i++) {
      botonesDistribuir[i].addEventListener("click", function () {
        var id = this.dataset.id;
        var desc = this.dataset.desc;
        var metodo = this.dataset.metodo;
        var monto = this.dataset.monto;
        var metodoRaw = this.dataset.metodoRaw;
        var unidadCausante = this.dataset.unidad;
        var tipoGasto = this.dataset.tipo;

        document.getElementById("gastoId").value = id;
        document.getElementById("tipoGastoRedirect").value = tipoGasto;
        document.getElementById("descGastoModal").innerHTML = "Se va a distribuir: <strong>" + desc + "</strong> (S/ " + monto + ")<br>Método: <strong>" + metodo + "</strong>";

        var grupoUnidad = document.getElementById("grupoUnidad");
        var inputUnidad = document.getElementById("unidadId");

        if (metodoRaw === "COBRO_DIRECTO") {
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

  var selectTipoGasto = document.getElementById("tipoGasto");
  var grupoIncidencia = document.getElementById("grupoIncidencia");

  if (selectTipoGasto && grupoIncidencia) {
    if (selectTipoGasto.value === "EXTRAORDINARIO") {
      grupoIncidencia.classList.remove("oculto");
    } else {
      grupoIncidencia.classList.add("oculto");
    }

    selectTipoGasto.addEventListener("change", function () {
      if (this.value === "EXTRAORDINARIO") {
        grupoIncidencia.classList.remove("oculto");
      } else {
        grupoIncidencia.classList.add("oculto");
        document.getElementById("incidenciaId").value = "";
      }
    });
  }

  var inputPeriodo = document.getElementById("periodo");
  if (inputPeriodo && !inputPeriodo.value) {
    var hoy = new Date();
    var anio = hoy.getFullYear();
    var mes = String(hoy.getMonth() + 1).padStart(2, "0");
    inputPeriodo.value = anio + "-" + mes;
  }

  var botonesDetallesUnidad = document.querySelectorAll(".boton-detalles");
  var modalDetalles = document.getElementById("modalDetalles");
  var botonCerrarDetalles = document.querySelector("#modalDetalles .cerrar-modal");

  if (botonesDetallesUnidad && modalDetalles && botonCerrarDetalles) {
    for (var k = 0; k < botonesDetallesUnidad.length; k++) {
      botonesDetallesUnidad[k].addEventListener("click", function (evento) {
        evento.preventDefault();

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

        modalDetalles.classList.add("modal-activo");
      });
    }

    botonCerrarDetalles.addEventListener("click", function () {
      modalDetalles.classList.remove("modal-activo");
    });

    window.addEventListener("click", function (evento) {
      if (evento.target == modalDetalles) {
        modalDetalles.classList.remove("modal-activo");
      }
    });
  }

  var condominioSelect = document.getElementById("condominioId");
  var torreSelect = document.getElementById("torre");
  var incidenciaSelect = document.getElementById("incidenciaId");

  if (condominioSelect) {
    var todasOpcionesTorre = [];
    if (torreSelect) {
      todasOpcionesTorre = Array.from(torreSelect.options).map(function (opt) {
        return opt.cloneNode(true);
      });
    }

    var todasOpcionesIncidencia = [];
    if (incidenciaSelect) {
      todasOpcionesIncidencia = Array.from(incidenciaSelect.options).map(function (opt) {
        return opt.cloneNode(true);
      });
    }

    function filtrarOpciones(preservarValor) {
      var condominioSeleccionado = condominioSelect.value;
      var torreActual = "";
      if (torreSelect) {
        torreActual = torreSelect.value;
        torreSelect.innerHTML = "";

        for (var p = 0; p < todasOpcionesTorre.length; p++) {
          var optT = todasOpcionesTorre[p];
          if (optT.value === "") {
            torreSelect.appendChild(optT.cloneNode(true));
          } else if (optT.getAttribute("data-condominio") === condominioSeleccionado) {
            torreSelect.appendChild(optT.cloneNode(true));
          }
        }

        if (preservarValor) {
          var existeOpcion = Array.from(torreSelect.options).some(function (o) {
            return o.value === torreActual;
          });
          torreSelect.value = existeOpcion ? torreActual : "";
        } else {
          torreSelect.value = "";
        }
        torreActual = torreSelect.value;
      }

      if (incidenciaSelect) {
        var incidenciaActual = incidenciaSelect.value;
        incidenciaSelect.innerHTML = "";

        for (var q = 0; q < todasOpcionesIncidencia.length; q++) {
          var optI = todasOpcionesIncidencia[q];
          if (optI.value === "") {
            incidenciaSelect.appendChild(optI.cloneNode(true));
          } else if (optI.getAttribute("data-condominio") === condominioSeleccionado) {
            var torreIncidencia = optI.getAttribute("data-torre");
            if (!torreActual || torreIncidencia === "" || torreIncidencia === torreActual) {
              incidenciaSelect.appendChild(optI.cloneNode(true));
            }
          }
        }

        if (preservarValor) {
          var existeOpcionIncidencia = Array.from(incidenciaSelect.options).some(function (o) {
            return o.value === incidenciaActual;
          });
          incidenciaSelect.value = existeOpcionIncidencia ? incidenciaActual : "";
        } else {
          incidenciaSelect.value = "";
        }
      }
    }

    condominioSelect.addEventListener("change", function () {
      filtrarOpciones(false);
    });
    if (torreSelect) {
      torreSelect.addEventListener("change", function () {
        filtrarOpciones(true);
      });
    }
    filtrarOpciones(true);
  }

  var condominioGenerar = document.getElementById("condominioGenerarId");
  var torreGenerar = document.getElementById("torreGenerarId");
  var unidadGenerar = document.getElementById("unidadId");

  if (condominioGenerar && torreGenerar && unidadGenerar) {
    var opcionesOriginalesUnidades = Array.from(unidadGenerar.options).filter(function (opt) {
      return opt.value !== "";
    });

    function actualizarTorresYUnidades() {
      var condominioSeleccionado = condominioGenerar.value;

      torreGenerar.innerHTML = '<option value="">Seleccione Torre...</option>';
      unidadGenerar.innerHTML = '<option value="">Seleccione...</option>';

      if (!condominioSeleccionado) {
        torreGenerar.disabled = true;
        unidadGenerar.disabled = true;
        return;
      }

      torreGenerar.disabled = false;
      unidadGenerar.disabled = true;
      var torresUnicas = new Set();
      var unidadesFiltradas = opcionesOriginalesUnidades.filter(function (opt) {
        return opt.getAttribute("data-condominio") === condominioSeleccionado;
      });

      for (var r = 0; r < unidadesFiltradas.length; r++) {
        var nombreTorre = unidadesFiltradas[r].getAttribute("data-torre");
        if (nombreTorre) {
          torresUnicas.add(nombreTorre);
        }
      }

      var arrayTorres = Array.from(torresUnicas).sort();
      for (var s = 0; s < arrayTorres.length; s++) {
        var opt = document.createElement("option");
        opt.value = arrayTorres[s];
        opt.textContent = arrayTorres[s];
        torreGenerar.appendChild(opt);
      }
    }

    function filtrarUnidadesPorTorre() {
      var condominioSeleccionado = condominioGenerar.value;
      var torreSeleccionada = torreGenerar.value;

      unidadGenerar.innerHTML = '<option value="">Seleccione...</option>';

      if (!torreSeleccionada) {
        unidadGenerar.disabled = true;
        return;
      }

      unidadGenerar.disabled = false;
      var unidadesFiltradas = opcionesOriginalesUnidades.filter(function (opt) {
        var coincideCondominio = opt.getAttribute("data-condominio") === condominioSeleccionado;
        var coincideTorre = opt.getAttribute("data-torre") === torreSeleccionada;
        return coincideCondominio && coincideTorre;
      });

      for (var t = 0; t < unidadesFiltradas.length; t++) {
        unidadGenerar.appendChild(unidadesFiltradas[t].cloneNode(true));
      }
    }

    condominioGenerar.addEventListener("change", actualizarTorresYUnidades);
    torreGenerar.addEventListener("change", filtrarUnidadesPorTorre);

    actualizarTorresYUnidades();
  }
});
