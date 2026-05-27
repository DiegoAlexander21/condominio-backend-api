function inicializarFiltrosReserva() {
  const condominio = document.getElementById("condominioId");
  const areaComun = document.getElementById("areaComunId");
  const unidad = document.getElementById("unidadId");

  if (!condominio || !areaComun || !unidad) {
    return;
  }

  const todasAreas = Array.from(areaComun.options).slice(1);
  const todasUnidades = Array.from(unidad.options).slice(1);

  function limpiarErrorEnReserva(elemento) {
    elemento.classList.remove("error-borde");
    const errorMsg = elemento.nextElementSibling;
    if (errorMsg && errorMsg.classList.contains("error-texto")) {
      errorMsg.style.display = "none";
    }
  }

  function actualizarFiltros() {
    const condominioSeleccionado = condominio.value;
    areaComun.innerHTML = '<option value="">Seleccione un área...</option>';
    const anteriorArea =
      areaComun.getAttribute("data-selected") || areaComun.value;

    if (condominioSeleccionado) {
      areaComun.disabled = false;
      const areasFiltradas = todasAreas.filter(
        (opt) => opt.getAttribute("data-condominio") === condominioSeleccionado,
      );
      areasFiltradas.forEach((opt) =>
        areaComun.appendChild(opt.cloneNode(true)),
      );
    } else {
      areaComun.disabled = true;
    }

    if (anteriorArea) {
      const opcionSeleccionada = Array.from(areaComun.options).find(
        (opt) => opt.value === anteriorArea,
      );
      areaComun.value = opcionSeleccionada ? anteriorArea : "";
      areaComun.setAttribute(
        "data-selected",
        opcionSeleccionada ? anteriorArea : "",
      );
    }

    unidad.innerHTML = '<option value="">Seleccione su unidad...</option>';
    const anteriorUnidad = unidad.getAttribute("data-selected") || unidad.value;

    if (condominioSeleccionado) {
      unidad.disabled = false;
      const unidadesFiltradas = todasUnidades.filter(
        (opt) => opt.getAttribute("data-condominio") === condominioSeleccionado,
      );
      unidadesFiltradas.forEach((opt) =>
        unidad.appendChild(opt.cloneNode(true)),
      );
    } else {
      unidad.disabled = true;
    }

    if (anteriorUnidad) {
      const opcionUnidad = Array.from(unidad.options).find(
        (opt) => opt.value === anteriorUnidad,
      );
      unidad.value = opcionUnidad ? anteriorUnidad : "";
      unidad.setAttribute("data-selected", opcionUnidad ? anteriorUnidad : "");
    }
  }

  condominio.addEventListener("change", function () {
    limpiarErrorEnReserva(condominio);
    areaComun.setAttribute("data-selected", "");
    unidad.setAttribute("data-selected", "");
    actualizarFiltros();
  });

  areaComun.addEventListener("change", function () {
    limpiarErrorEnReserva(areaComun);
    areaComun.setAttribute("data-selected", areaComun.value);
  });

  unidad.addEventListener("change", function () {
    limpiarErrorEnReserva(unidad);
    unidad.setAttribute("data-selected", unidad.value);
  });

  const areaInicial = areaComun.value;
  if (areaInicial) {
    areaComun.setAttribute("data-selected", areaInicial);
  }

  const unidadInicial = unidad.value;
  if (unidadInicial) {
    unidad.setAttribute("data-selected", unidadInicial);
  }

  actualizarFiltros();
}
