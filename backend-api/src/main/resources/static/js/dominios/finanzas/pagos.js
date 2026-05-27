document.addEventListener("DOMContentLoaded", function () {
  if (typeof inicializarPagoFormulario === "function") {
    inicializarPagoFormulario();
  }

  if (typeof inicializarEvidenciasPago === "function") {
    inicializarEvidenciasPago();
  }

  if (typeof inicializarModalEvidenciaPago === "function") {
    inicializarModalEvidenciaPago();
  }
});
