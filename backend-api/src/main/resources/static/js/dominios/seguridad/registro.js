document.addEventListener("DOMContentLoaded", function () {
    const tipoDocumento = document.getElementById("tipoDocumento");
    const numeroDocumento = document.getElementById("numeroDocumento");
    const telefono = document.getElementById("telefono");

    function actualizarRestriccionesDocumento() {
        if (tipoDocumento.value === "DNI") {
            numeroDocumento.setAttribute("maxlength", "8");
            numeroDocumento.value = numeroDocumento.value.replace(/\D/g, "").slice(0, 8);
        } else if (tipoDocumento.value === "CE") {
            numeroDocumento.setAttribute("maxlength", "12");
        }
    }

    tipoDocumento.addEventListener("change", function () {
        numeroDocumento.value = ""; 
        actualizarRestriccionesDocumento();
    });

    numeroDocumento.addEventListener("input", function () {
        if (tipoDocumento.value === "DNI") {
            this.value = this.value.replace(/\D/g, ""); 
        } else {
            this.value = this.value.replace(/[^A-Za-z0-9]/g, ""); 
        }
    });

    telefono.addEventListener("input", function () {
        this.value = this.value.replace(/\D/g, "").slice(0, 9);
    });

    actualizarRestriccionesDocumento();
});
