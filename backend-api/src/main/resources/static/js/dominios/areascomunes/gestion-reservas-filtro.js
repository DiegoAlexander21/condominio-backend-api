function inicializarFiltroAreasComunes() {
  const selectCondominio = document.getElementById("condominioId");
  const selectArea = document.getElementById("areaComunId");

  if (!selectCondominio || !selectArea) {
    return;
  }

  const opcionesArea = Array.from(selectArea.options).slice(1);

  function filtrarAreas() {
    const condominioId = selectCondominio.value;

    if (condominioId) {
      selectArea.disabled = false;
    } else {
      selectArea.disabled = true;
      selectArea.value = "";
    }

    opcionesArea.forEach((opcion) => {
      if (
        condominioId &&
        opcion.getAttribute("data-condominio") === condominioId
      ) {
        opcion.style.display = "";
      } else {
        opcion.style.display = "none";
      }
    });

    const opcionSeleccionada = selectArea.options[selectArea.selectedIndex];
    if (opcionSeleccionada && opcionSeleccionada.style.display === "none") {
      selectArea.value = "";
    }
  }

  selectCondominio.addEventListener("change", filtrarAreas);
  filtrarAreas();
}
