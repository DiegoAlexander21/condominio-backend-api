document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("formHistorial");
    const inputDepto = document.getElementById("departamentoId");
    const containerMsj = document.getElementById("container-msj");

    if (inputDepto) {
        inputDepto.addEventListener("focus", function () {
            if (this.value === "0") this.value = "";
        });

        inputDepto.addEventListener("input", function () {
            let valor = this.value ? this.value.trim() : "";
            if (/^0+\d+$/.test(valor)) this.value = String(Number(valor));
        });
    }

    function mostrarErrorTemporal(mensaje) {
        const p = document.createElement("p");
        p.className = "alert error";
        p.textContent = mensaje;
        containerMsj.appendChild(p);
        setTimeout(() => {
            p.style.transition = "opacity 0.5s ease";
            p.style.opacity = "0";
            setTimeout(() => p.remove(), 500);
        }, 3000);
    }

    form.addEventListener("submit", function (event) {
        const anterior = document.getElementById("propietarioAnterior").value.trim();
        const nuevo = document.getElementById("nuevoPropietario").value.trim();
        const depto = inputDepto.value;
        const soloLetras = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$/;

        if (!depto || depto <= 0) {
            mostrarErrorTemporal("Por favor, ingrese un número de departamento válido.");
            event.preventDefault();
            return;
        }

        if (anterior === "" || nuevo === "") {
            mostrarErrorTemporal("Ambos nombres de propietarios son obligatorios.");
            event.preventDefault();
            return;
        }

        if (!soloLetras.test(anterior) || !soloLetras.test(nuevo)) {
            mostrarErrorTemporal("Los nombres solo pueden contener letras y espacios.");
            event.preventDefault();
            return;
        }

        if (anterior.toLowerCase() === nuevo.toLowerCase()) {
            mostrarErrorTemporal("El nuevo dueño no puede ser la misma persona que el anterior.");
            event.preventDefault();
            return;
        }
    });
});