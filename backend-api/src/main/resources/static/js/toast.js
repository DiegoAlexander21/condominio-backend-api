// toast.js — sistema de notificaciones compartido

function mostrarToast(mensaje, tipo) {
  var contenedor = document.getElementById("toast-contenedor");
  if (!contenedor) return;

  var toast = document.createElement("div");
  toast.className = "toast toast-" + tipo;
  toast.textContent = mensaje;

  contenedor.appendChild(toast);

  toast.getBoundingClientRect();
  toast.classList.add("toast-visible");

  var timer = setTimeout(function () {
    cerrarToast(toast);
  }, 4000);

  toast.addEventListener("click", function () {
    clearTimeout(timer);
    cerrarToast(toast);
  });

  toast.title = "Clic para cerrar";
  toast.style.cursor = "pointer";
}

function cerrarToast(toast) {
  toast.classList.remove("toast-visible");
  toast.classList.add("toast-oculto");
  setTimeout(function () {
    if (toast.parentNode) toast.parentNode.removeChild(toast);
  }, 350);
}

document.addEventListener("DOMContentLoaded", function () {
  var alertaExito = document.getElementById("msg-exito");
  var alertaError = document.getElementById("msg-error");

  if (alertaExito) mostrarToast(alertaExito.textContent.trim(), "exito");
  if (alertaError) mostrarToast(alertaError.textContent.trim(), "error");
});
