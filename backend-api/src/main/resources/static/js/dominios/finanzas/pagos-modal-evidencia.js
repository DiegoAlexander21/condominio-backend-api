function inicializarModalEvidenciaPago() {
  var modalEvidencia = document.getElementById("modalEvidencia");
  var cerrarEvidencia = document.querySelector("#modalEvidencia .cerrar-modal");
  var botonesVerEvidencia = document.querySelectorAll(".boton-evidencia");

  if (!modalEvidencia || !cerrarEvidencia || !botonesVerEvidencia.length) {
    return;
  }

  for (var u = 0; u < botonesVerEvidencia.length; u++) {
    botonesVerEvidencia[u].addEventListener("click", function (eventoEv) {
      eventoEv.preventDefault();
      var enlaces = this.getAttribute("data-urls");
      var contenedor = document.getElementById("contenedorEvidenciaModal");
      contenedor.innerHTML = "";

      if (enlaces && enlaces.trim() !== "") {
        var listaEnlaces = enlaces.split(",");
        listaEnlaces.forEach(function (enlace) {
          if (enlace.trim() !== "") {
            var contenedorImagen = document.createElement("div");
            contenedorImagen.style.marginBottom = "15px";
            contenedorImagen.style.textAlign = "center";

            var imagenElemento = document.createElement("img");
            imagenElemento.src = enlace.trim();
            imagenElemento.style.maxWidth = "100%";
            imagenElemento.style.borderRadius = "8px";
            imagenElemento.style.boxShadow = "0 4px 6px rgba(0,0,0,0.1)";
            imagenElemento.style.cursor = "pointer";
            imagenElemento.onclick = function () {
              window.open(this.src, "_blank");
            };

            contenedorImagen.appendChild(imagenElemento);
            contenedor.appendChild(contenedorImagen);
          }
        });
      } else {
        contenedor.innerHTML =
          "<p>No hay evidencias registradas para este pago.</p>";
      }

      modalEvidencia.classList.add("modal-activo");
    });
  }

  cerrarEvidencia.addEventListener("click", function () {
    modalEvidencia.classList.remove("modal-activo");
  });

  window.addEventListener("click", function (evento) {
    if (evento.target == modalEvidencia) {
      modalEvidencia.classList.remove("modal-activo");
    }
  });
}
