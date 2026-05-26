document.addEventListener("DOMContentLoaded", function () {
  const condominio = document.getElementById("condominioId");
  const areaComun = document.getElementById("areaComunId");
  const unidad = document.getElementById("unidadId");

  const todasAreas = Array.from(areaComun.options).slice(1);
  const todasUnidades = Array.from(unidad.options).slice(1);

  function limpiarError(elemento) {
      elemento.classList.remove("error-borde");
      const errorMsg = elemento.nextElementSibling;
      if (errorMsg && errorMsg.classList.contains("error-texto")) {
          errorMsg.style.display = "none";
      }
  }

  function actualizarFiltros() {
      const selectedCondominio = condominio.value;
      areaComun.innerHTML = '<option value="">Seleccione un área...</option>';
      let previousAreaValue = areaComun.getAttribute('data-selected') || areaComun.value;

      if (selectedCondominio) {
          areaComun.disabled = false;
          const filteredAreas = todasAreas.filter(opt => opt.getAttribute("data-condominio") === selectedCondominio);
          filteredAreas.forEach(opt => areaComun.appendChild(opt.cloneNode(true)));
      } else {
          areaComun.disabled = true;
      }

      if (previousAreaValue) {
          const optionToSelect = Array.from(areaComun.options).find(opt => opt.value === previousAreaValue);
          if (optionToSelect) {
              areaComun.value = previousAreaValue;
          } else {
              areaComun.value = "";
              areaComun.setAttribute('data-selected', '');
          }
      }

      unidad.innerHTML = '<option value="">Seleccione su unidad...</option>';
      let previousUnidadValue = unidad.getAttribute('data-selected') || unidad.value;

      if (selectedCondominio) {
          unidad.disabled = false;
          const filteredUnidades = todasUnidades.filter(opt => opt.getAttribute("data-condominio") === selectedCondominio);
          filteredUnidades.forEach(opt => unidad.appendChild(opt.cloneNode(true)));
      } else {
          unidad.disabled = true;
      }

      if (previousUnidadValue) {
          const optionToSelect = Array.from(unidad.options).find(opt => opt.value === previousUnidadValue);
          if (optionToSelect) {
              unidad.value = previousUnidadValue;
          } else {
              unidad.value = "";
              unidad.setAttribute('data-selected', '');
          }
      }
  }

  condominio.addEventListener("change", function () {
      limpiarError(condominio);
      areaComun.setAttribute('data-selected', '');
      unidad.setAttribute('data-selected', '');
      actualizarFiltros();
  });

  areaComun.addEventListener("change", function () {
      limpiarError(areaComun);
      areaComun.setAttribute('data-selected', areaComun.value);
  });

  unidad.addEventListener("change", function () {
      limpiarError(unidad);
      unidad.setAttribute('data-selected', unidad.value);
  });

    const initialAreaValue = areaComun.value;
    if (initialAreaValue) {
        areaComun.setAttribute('data-selected', initialAreaValue);
    }
    const initialUnidadValue = unidad.value;
    if (initialUnidadValue) {
        unidad.setAttribute('data-selected', initialUnidadValue);
    }

    actualizarFiltros();

    const fechaReserva = document.getElementById("fechaReserva");
    const horaInicio = document.getElementById("horaInicio");
    const horaFin = document.getElementById("horaFin");
    let reservasDelDia = [];
    let horariosArea = { inicio: null, fin: null };

    function horaAMinutos(hora) {
        if (!hora) return 0;
        const partes = hora.split(':');
        const h = parseInt(partes[0], 10);
        const m = parseInt(partes[1], 10);
        return h * 60 + m;
    }

    function minutosAHora(minutos) {
        const h = Math.floor(minutos / 60).toString().padStart(2, '0');
        const m = (minutos % 60).toString().padStart(2, '0');
        return `${h}:${m}`;
    }

    function cargarHorarios() {
        const areaSeleccionada = areaComun.options[areaComun.selectedIndex];
        const fecha = fechaReserva.value;

        if (!areaSeleccionada || !areaSeleccionada.value || !fecha) {
            horaInicio.innerHTML = '<option value="">Seleccione hora inicio...</option>';
            horaFin.innerHTML = '<option value="">Seleccione hora fin...</option>';
            horaInicio.disabled = true;
            horaFin.disabled = true;
            return;
        }

        const inicioStr = areaSeleccionada.getAttribute('data-hora-inicio');
        const finStr = areaSeleccionada.getAttribute('data-hora-fin');

        if (!inicioStr || !finStr) return;

        horariosArea.inicio = horaAMinutos(inicioStr.substring(0, 5));
        horariosArea.fin = horaAMinutos(finStr.substring(0, 5));

        fetch(`/reservas/api/reservas?areaComunId=${areaSeleccionada.value}&fecha=${fecha}`)
            .then(res => res.json())
            .then(data => {
                reservasDelDia = data.map(r => ({
                    inicio: horaAMinutos(r.horaInicio.substring(0, 5)),
                    fin: horaAMinutos(r.horaFin.substring(0, 5))
                }));
                actualizarOpcionesInicio();
            })
            .catch(err => {
                console.error("Error al obtener reservas", err);
                reservasDelDia = [];
                actualizarOpcionesInicio();
            });
    }

    function actualizarOpcionesInicio() {
        const anteriorInicio = horaInicio.getAttribute('data-selected') || horaInicio.value;
        horaInicio.innerHTML = '<option value="">Seleccione hora inicio...</option>';
        
        for (let m = horariosArea.inicio; m < horariosArea.fin; m += 30) {
            const mFin = m + 30;
            const choca = reservasDelDia.some(r => (m < r.fin && mFin > r.inicio));
            
            if (!choca) {
                const valor = minutosAHora(m) + ":00";
                const option = document.createElement("option");
                option.value = valor;
                option.textContent = minutosAHora(m);
                horaInicio.appendChild(option);
            }
        }
        
        horaInicio.disabled = false;
        
        if (anteriorInicio) {
            const optionToSelect = Array.from(horaInicio.options).find(o => o.value.startsWith(anteriorInicio.substring(0, 5)));
            if (optionToSelect) {
                horaInicio.value = optionToSelect.value;
            }
        }
        
        actualizarOpcionesFin();
    }

    function actualizarOpcionesFin() {
        const inicioSeleccionadoStr = horaInicio.value;
        const anteriorFin = horaFin.getAttribute('data-selected') || horaFin.value;
        horaFin.innerHTML = '<option value="">Seleccione hora fin...</option>';

        if (!inicioSeleccionadoStr) {
            horaFin.disabled = true;
            return;
        }

        const inicioMinutos = horaAMinutos(inicioSeleccionadoStr.substring(0, 5));
        
        let finMaximoPermitido = horariosArea.fin;
        const reservasFuturas = reservasDelDia.filter(r => r.inicio >= inicioMinutos);
        if (reservasFuturas.length > 0) {
            reservasFuturas.sort((a, b) => a.inicio - b.inicio);
            finMaximoPermitido = reservasFuturas[0].inicio;
        }

        for (let m = inicioMinutos + 30; m <= finMaximoPermitido; m += 30) {
            const valor = minutosAHora(m) + ":00";
            const option = document.createElement("option");
            option.value = valor;
            option.textContent = minutosAHora(m);
            horaFin.appendChild(option);
        }
        
        horaFin.disabled = false;

        if (anteriorFin) {
            const optionToSelect = Array.from(horaFin.options).find(o => o.value.startsWith(anteriorFin.substring(0, 5)));
            if (optionToSelect) {
                horaFin.value = optionToSelect.value;
            }
        }
    }

    areaComun.addEventListener("change", cargarHorarios);
    fechaReserva.addEventListener("change", cargarHorarios);
    
    horaInicio.addEventListener("change", function() {
        horaInicio.setAttribute('data-selected', horaInicio.value);
        actualizarOpcionesFin();
    });
    
    horaFin.addEventListener("change", function() {
        horaFin.setAttribute('data-selected', horaFin.value);
    });

    const initialInicioValue = horaInicio.value || horaInicio.getAttribute('value');
    if (initialInicioValue) horaInicio.setAttribute('data-selected', initialInicioValue);
    
    const initialFinValue = horaFin.value || horaFin.getAttribute('value');
    if (initialFinValue) horaFin.setAttribute('data-selected', initialFinValue);

    if (areaComun.value && fechaReserva.value) {
        cargarHorarios();
    }
});
