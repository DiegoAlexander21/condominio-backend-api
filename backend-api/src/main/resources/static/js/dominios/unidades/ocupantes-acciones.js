function inicializarAccionesOcupantes() {
  var botonRevertir = document.getElementById("btnRevertir");
  var botonLimpiarTodo = document.getElementById("btnLimpiarTodo");
  var botonLimpiarResidente = document.getElementById("btnLimpiarResidente");

  if (botonRevertir) {
    botonRevertir.addEventListener("click", function () {
      var confirmacion = confirm(
        "¿Estas seguro de que deseas deshacer los cambios no guardados y restaurar los valores originales?",
      );
      if (confirmacion) {
        document.getElementById("formularioOcupantes").reset();
      }
    });
  }

  if (botonLimpiarTodo) {
    botonLimpiarTodo.addEventListener("click", function () {
      var confirmacion = confirm(
        '¿Estas seguro de que quieres vaciar todos los campos del propietario y residente? (Los cambios no se guardaran hasta hacer clic en "Guardar")',
      );
      if (confirmacion) {
        document.getElementById("nombrePropietario").value = "";
        document.getElementById("dniPropietario").value = "";
        document.getElementById("emailPropietario").value = "";
        document.getElementById("telefonoPropietario").value = "";
        limpiarCamposResidente();
      }
    });
  }

  if (botonLimpiarResidente) {
    botonLimpiarResidente.addEventListener("click", function () {
      var confirmacion = confirm(
        "¿Estas seguro de que quieres vaciar los datos del residente?",
      );
      if (confirmacion) {
        limpiarCamposResidente();
      }
    });
  }

  function limpiarCamposResidente() {
    document.getElementById("nombreResidente").value = "";
    document.getElementById("dniResidente").value = "";
    document.getElementById("emailResidente").value = "";
    document.getElementById("parentesco").value = "";
    document.getElementById("residenteActivo").value = "false";
  }
}
