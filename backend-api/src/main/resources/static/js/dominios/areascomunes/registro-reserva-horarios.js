function inicializarHorariosReserva() {
  const areaComun = document.getElementById("areaComunId");
  const fechaReserva = document.getElementById("fechaReserva");
  const horaInicio = document.getElementById("horaInicio");
  const horaFin = document.getElementById("horaFin");

  if (!areaComun || !fechaReserva || !horaInicio || !horaFin) {
    return;
  }

  let reservasDelDia = [];
  const horariosArea = { inicio: null, fin: null };

  function horaAMinutos(hora) {
    if (!hora) return 0;
    const partes = hora.split(":");
    const h = parseInt(partes[0], 10);
    const m = parseInt(partes[1], 10);
    return h * 60 + m;
  }

  function minutosAHora(minutos) {
    const h = Math.floor(minutos / 60)
      .toString()
      .padStart(2, "0");
    const m = (minutos % 60).toString().padStart(2, "0");
    return `${h}:${m}`;
  }

  function cargarHorarios() {
    const areaSeleccionada = areaComun.options[areaComun.selectedIndex];
    const fecha = fechaReserva.value;

    if (!areaSeleccionada || !areaSeleccionada.value || !fecha) {
      horaInicio.innerHTML =
        '<option value="">Seleccione hora inicio...</option>';
      horaFin.innerHTML = '<option value="">Seleccione hora fin...</option>';
      horaInicio.disabled = true;
      horaFin.disabled = true;
      return;
    }

    const inicioStr = areaSeleccionada.getAttribute("data-hora-inicio");
    const finStr = areaSeleccionada.getAttribute("data-hora-fin");

    if (!inicioStr || !finStr) return;

    horariosArea.inicio = horaAMinutos(inicioStr.substring(0, 5));
    horariosArea.fin = horaAMinutos(finStr.substring(0, 5));

    fetch(
      `/reservas/api/reservas?areaComunId=${areaSeleccionada.value}&fecha=${fecha}`,
    )
      .then((res) => res.json())
      .then((data) => {
        reservasDelDia = data.map((r) => ({
          inicio: horaAMinutos(r.horaInicio.substring(0, 5)),
          fin: horaAMinutos(r.horaFin.substring(0, 5)),
        }));
        actualizarOpcionesInicio();
      })
      .catch((err) => {
        console.error("Error al obtener reservas", err);
        reservasDelDia = [];
        actualizarOpcionesInicio();
      });
  }

  function actualizarOpcionesInicio() {
    const anteriorInicio =
      horaInicio.getAttribute("data-selected") || horaInicio.value;
    horaInicio.innerHTML =
      '<option value="">Seleccione hora inicio...</option>';

    for (let m = horariosArea.inicio; m < horariosArea.fin; m += 30) {
      const mFin = m + 30;
      const choca = reservasDelDia.some((r) => m < r.fin && mFin > r.inicio);

      if (!choca) {
        const valor = `${minutosAHora(m)}:00`;
        const option = document.createElement("option");
        option.value = valor;
        option.textContent = minutosAHora(m);
        horaInicio.appendChild(option);
      }
    }

    horaInicio.disabled = false;

    if (anteriorInicio) {
      const opcionInicio = Array.from(horaInicio.options).find((o) =>
        o.value.startsWith(anteriorInicio.substring(0, 5)),
      );
      if (opcionInicio) {
        horaInicio.value = opcionInicio.value;
      }
    }

    actualizarOpcionesFin();
  }

  function actualizarOpcionesFin() {
    const inicioSeleccionado = horaInicio.value;
    const anteriorFin = horaFin.getAttribute("data-selected") || horaFin.value;
    horaFin.innerHTML = '<option value="">Seleccione hora fin...</option>';

    if (!inicioSeleccionado) {
      horaFin.disabled = true;
      return;
    }

    const inicioMinutos = horaAMinutos(inicioSeleccionado.substring(0, 5));

    let finMaximoPermitido = horariosArea.fin;
    const reservasFuturas = reservasDelDia.filter(
      (r) => r.inicio >= inicioMinutos,
    );
    if (reservasFuturas.length > 0) {
      reservasFuturas.sort((a, b) => a.inicio - b.inicio);
      finMaximoPermitido = reservasFuturas[0].inicio;
    }

    for (let m = inicioMinutos + 30; m <= finMaximoPermitido; m += 30) {
      const valor = `${minutosAHora(m)}:00`;
      const option = document.createElement("option");
      option.value = valor;
      option.textContent = minutosAHora(m);
      horaFin.appendChild(option);
    }

    horaFin.disabled = false;

    if (anteriorFin) {
      const opcionFin = Array.from(horaFin.options).find((o) =>
        o.value.startsWith(anteriorFin.substring(0, 5)),
      );
      if (opcionFin) {
        horaFin.value = opcionFin.value;
      }
    }
  }

  areaComun.addEventListener("change", cargarHorarios);
  fechaReserva.addEventListener("change", cargarHorarios);

  horaInicio.addEventListener("change", function () {
    horaInicio.setAttribute("data-selected", horaInicio.value);
    actualizarOpcionesFin();
  });

  horaFin.addEventListener("change", function () {
    horaFin.setAttribute("data-selected", horaFin.value);
  });

  const inicioInicial = horaInicio.value || horaInicio.getAttribute("value");
  if (inicioInicial) horaInicio.setAttribute("data-selected", inicioInicial);

  const finInicial = horaFin.value || horaFin.getAttribute("value");
  if (finInicial) horaFin.setAttribute("data-selected", finInicial);

  if (areaComun.value && fechaReserva.value) {
    cargarHorarios();
  }
}
