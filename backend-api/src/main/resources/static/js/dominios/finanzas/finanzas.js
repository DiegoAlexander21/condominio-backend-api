document.addEventListener('DOMContentLoaded', function() {
    const modalDistribucion = document.getElementById('modalDistribucion');
    const botonesDistribuir = document.querySelectorAll('.boton-distribuir');
    const cerrarModal = document.querySelector('.cerrar-modal');
    
    if (modalDistribucion && botonesDistribuir) {
        botonesDistribuir.forEach(boton => {
            boton.addEventListener('click', function() {
                const id = this.dataset.id;
                const desc = this.dataset.desc;
                const metodo = this.dataset.metodo;
                const monto = this.dataset.monto;
                const metodoRaw = this.dataset.metodoRaw;
                const unidadCausante = this.dataset.unidad;
                const tipoGasto = this.dataset.tipo;

                document.getElementById('gastoId').value = id;
                document.getElementById('tipoGastoRedirect').value = tipoGasto;
                document.getElementById('descGastoModal').innerHTML = `Se va a distribuir: <strong>${desc}</strong> (S/ ${monto})<br>Método: <strong>${metodo}</strong>`;

                const grupoUnidad = document.getElementById('grupoUnidad');
                const inputUnidad = document.getElementById('unidadId');
                
                if (metodoRaw === 'COBRO_DIRECTO') {
                    grupoUnidad.classList.remove('oculto');
                    inputUnidad.required = true;
                    if (unidadCausante && unidadCausante !== 'null') {
                        inputUnidad.value = unidadCausante;
                        inputUnidad.style.pointerEvents = 'none';
                        inputUnidad.style.backgroundColor = '#e9ecef';
                    } else {
                        inputUnidad.value = '';
                        inputUnidad.style.pointerEvents = 'auto';
                        inputUnidad.style.backgroundColor = '';
                    }
                } else {
                    grupoUnidad.classList.add('oculto');
                    inputUnidad.required = false;
                    inputUnidad.value = '';
                    inputUnidad.style.pointerEvents = 'auto';
                    inputUnidad.style.backgroundColor = '';
                }

                modalDistribucion.style.display = 'block';
            });
        });

        if (cerrarModal) {
            cerrarModal.addEventListener('click', function() {
                modalDistribucion.style.display = 'none';
            });
        }

        window.addEventListener('click', function(e) {
            if (e.target == modalDistribucion) {
                modalDistribucion.style.display = 'none';
            }
        });
    }

    const selectTipoGasto = document.getElementById('tipoGasto');
    const grupoIncidencia = document.getElementById('grupoIncidencia');
    
    if (selectTipoGasto && grupoIncidencia) {
        if (selectTipoGasto.value === 'EXTRAORDINARIO') {
            grupoIncidencia.classList.remove('oculto');
        } else {
            grupoIncidencia.classList.add('oculto');
        }

        selectTipoGasto.addEventListener('change', function() {
            if (this.value === 'EXTRAORDINARIO') {
                grupoIncidencia.classList.remove('oculto');
            } else {
                grupoIncidencia.classList.add('oculto');
                document.getElementById('incidenciaId').value = '';
            }
        });
    }

    const inputPeriodo = document.getElementById('periodo');
    if (inputPeriodo && !inputPeriodo.value) {
        const hoy = new Date();
        const anio = hoy.getFullYear();
        const mes = String(hoy.getMonth() + 1).padStart(2, '0');
        inputPeriodo.value = `${anio}-${mes}`;
    }

    const botonesDetallesUnidad = document.querySelectorAll('.boton-detalles');
    const modal = document.getElementById("modalDetalles");
    const botonCerrar = document.querySelector(".cerrar-modal");

    if (botonesDetallesUnidad && modal && botonCerrar) {
        botonesDetallesUnidad.forEach(boton => {
            boton.addEventListener('click', function(e) {
                e.preventDefault();
                
                document.getElementById("det-condominio").textContent = this.getAttribute("data-condominio");
                document.getElementById("det-unidad").textContent = this.getAttribute("data-unidad");
                document.getElementById("det-torre").textContent = this.getAttribute("data-torre");
                document.getElementById("det-piso").textContent = this.getAttribute("data-piso");
                document.getElementById("det-area").textContent = this.getAttribute("data-area");
                document.getElementById("det-estado").textContent = this.getAttribute("data-estado");
                
                document.getElementById("det-propietario").textContent = this.getAttribute("data-propietario") || "Sin registrar";
                document.getElementById("det-dni-prop").textContent = this.getAttribute("data-dni-prop") || "-";
                document.getElementById("det-tel-prop").textContent = this.getAttribute("data-tel-prop") || "-";
                document.getElementById("det-email-prop").textContent = this.getAttribute("data-email-prop") || "-";
                
                let residente = this.getAttribute("data-residente");
                if (!residente || residente === "null" || residente.trim() === "") {
                    document.getElementById("det-residente").textContent = "Sin residente registrado";
                    document.getElementById("det-dni-res").textContent = "-";
                    document.getElementById("det-tel-res").textContent = "-";
                    document.getElementById("det-parentesco").textContent = "-";
                } else {
                    document.getElementById("det-residente").textContent = residente;
                    document.getElementById("det-dni-res").textContent = this.getAttribute("data-dni-res") || "-";
                    document.getElementById("det-tel-res").textContent = this.getAttribute("data-tel-res") || "-";
                    document.getElementById("det-parentesco").textContent = this.getAttribute("data-parentesco") || "-";
                }

                modal.style.display = "block";
            });
        });

        botonCerrar.addEventListener("click", function() {
            modal.style.display = "none";
        });
        
        window.addEventListener('click', function(event) {
            if (event.target == modal) {
                modal.style.display = "none";
            }
        });
    }

    const condominioSelect = document.getElementById('condominioId');
    const torreSelect = document.getElementById('torre');
    const incidenciaSelect = document.getElementById('incidenciaId');
    
    if (condominioSelect) {
        let allTorreOptions = [];
        if (torreSelect) {
            allTorreOptions = Array.from(torreSelect.options).map(opt => opt.cloneNode(true));
        }

        let allIncidenciaOptions = [];
        if (incidenciaSelect) {
            allIncidenciaOptions = Array.from(incidenciaSelect.options).map(opt => opt.cloneNode(true));
        }

        function filterOptions(preserveValue) {
            const selectedCondominio = condominioSelect.value;
            let currentTorre = "";
            if (torreSelect) {
                currentTorre = torreSelect.value;
                torreSelect.innerHTML = '';
                
                allTorreOptions.forEach(opt => {
                    if (opt.value === "") {
                        torreSelect.appendChild(opt.cloneNode(true));
                    } else if (opt.getAttribute('data-condominio') === selectedCondominio) {
                        torreSelect.appendChild(opt.cloneNode(true));
                    }
                });
                
                if (preserveValue) {
                    const optionExists = Array.from(torreSelect.options).some(o => o.value === currentTorre);
                    torreSelect.value = optionExists ? currentTorre : '';
                } else {
                    torreSelect.value = '';
                }
                currentTorre = torreSelect.value;
            }

            if (incidenciaSelect) {
                const currentIncidencia = incidenciaSelect.value;
                incidenciaSelect.innerHTML = '';
                
                allIncidenciaOptions.forEach(opt => {
                    if (opt.value === "") {
                        incidenciaSelect.appendChild(opt.cloneNode(true));
                    } else if (opt.getAttribute('data-condominio') === selectedCondominio) {
                        const incidenciaTorre = opt.getAttribute('data-torre');
                        if (!currentTorre || incidenciaTorre === "" || incidenciaTorre === currentTorre) {
                            incidenciaSelect.appendChild(opt.cloneNode(true));
                        }
                    }
                });
                
                if (preserveValue) {
                    const optionExists = Array.from(incidenciaSelect.options).some(o => o.value === currentIncidencia);
                    incidenciaSelect.value = optionExists ? currentIncidencia : '';
                } else {
                    incidenciaSelect.value = '';
                }
            }
        }
        
        condominioSelect.addEventListener('change', () => filterOptions(false));
        if (torreSelect) {
            torreSelect.addEventListener('change', () => filterOptions(true));
        }
        filterOptions(true);
    }



    const btnPagoTotal = document.getElementById('btnPagoTotal');
    if (btnPagoTotal) {
        btnPagoTotal.addEventListener('click', () => {
            document.getElementById('monto').value = btnPagoTotal.getAttribute('data-saldo');
        });
    }

    const btnSeleccionarArchivos = document.getElementById('btnSeleccionarArchivos');
    const inputArchivos = document.getElementById('evidenciaArchivos');
    const contenedorVistaPrevia = document.getElementById('evidenciaPreview');
    const formPago = document.querySelector('.form-contenedor-margen');
    const urlEvidencia = document.getElementById('urlEvidencia');

    if (btnSeleccionarArchivos && inputArchivos && formPago) {
        let archivosPendientes = [];

        btnSeleccionarArchivos.addEventListener('click', function(e) {
            e.preventDefault();
            inputArchivos.click();
        });

        inputArchivos.addEventListener('change', function() {
            const nuevosArchivos = Array.from(this.files);
            
            if (archivosPendientes.length + nuevosArchivos.length > 5) {
                alert('Solo puedes subir un máximo de 5 imágenes en total.');
                return;
            }

            nuevosArchivos.forEach(archivo => {
                if (archivo.size > 10000000) {
                    alert('El archivo ' + archivo.name + ' excede el tamaño máximo de 10MB.');
                    return;
                }
                
                archivosPendientes.push(archivo);
                
                const contenedorImagen = document.createElement('div');
                contenedorImagen.className = 'contenedor-imagen-previa';
                
                const imagen = document.createElement('img');
                imagen.src = URL.createObjectURL(archivo);
                imagen.style.cursor = 'pointer';
                imagen.onclick = function() { window.open(this.src, '_blank'); };
                
                const botonEliminar = document.createElement('button');
                botonEliminar.innerHTML = '&times;';
                botonEliminar.className = 'boton-eliminar-imagen';
                botonEliminar.onclick = function(e) {
                    e.preventDefault();
                    contenedorImagen.remove();
                    archivosPendientes = archivosPendientes.filter(f => f !== archivo);
                };

                contenedorImagen.appendChild(imagen);
                contenedorImagen.appendChild(botonEliminar);
                contenedorVistaPrevia.appendChild(contenedorImagen);
            });
            
            this.value = '';
        });

        formPago.addEventListener('submit', async function(e) {
            const inputMonto = document.getElementById('monto');
            const btnPagoTotal = document.getElementById('btnPagoTotal');
            const saldoPendiente = btnPagoTotal ? parseFloat(btnPagoTotal.getAttribute('data-saldo')) : null;

            if (inputMonto && saldoPendiente !== null) {
                const montoIngresado = parseFloat(inputMonto.value);
                if (isNaN(montoIngresado) || montoIngresado <= 0) {
                    alert('El monto ingresado debe ser mayor a cero.');
                    e.preventDefault();
                    return;
                }
                if (montoIngresado > saldoPendiente) {
                    alert('El monto del pago (S/ ' + montoIngresado.toFixed(2) + ') no puede ser mayor al saldo pendiente (S/ ' + saldoPendiente.toFixed(2) + ').');
                    e.preventDefault();
                    return;
                }
            }

            if (archivosPendientes.length === 0) {
                return; 
            }

            e.preventDefault();
            const btnSubir = formPago.querySelector('button[type="submit"]');
            const textoOriginal = btnSubir.innerHTML;
            btnSubir.innerHTML = 'Subiendo comprobantes...';
            btnSubir.disabled = true;

            const nombreNube = btnSeleccionarArchivos.dataset.cloudName;
            const presetSubida = btnSeleccionarArchivos.dataset.uploadPreset;
            const enlaces = [];

            try {
                for (const archivo of archivosPendientes) {
                    const datosFormulario = new FormData();
                    datosFormulario.append('file', archivo);
                    datosFormulario.append('upload_preset', presetSubida);

                    const respuesta = await fetch(`https://api.cloudinary.com/v1_1/${nombreNube}/image/upload`, {
                        method: 'POST',
                        body: datosFormulario
                    });

                    if (!respuesta.ok) {
                        throw new Error('Error al subir ' + archivo.name);
                    }

                    const datos = await respuesta.json();
                    enlaces.push(datos.secure_url);
                }

                urlEvidencia.value = enlaces.join(',');
                formPago.submit();
            } catch (errorSubida) {
                console.error('Error subiendo imágenes:', errorSubida);
                alert('Hubo un error subiendo los comprobantes. Por favor, intenta de nuevo.');
                btnSubir.innerHTML = textoOriginal;
                btnSubir.disabled = false;
            }
        });
    }

    const condominioGenerar = document.getElementById('condominioGenerarId');
    const torreGenerar = document.getElementById('torreGenerarId');
    const unidadGenerar = document.getElementById('unidadId');

    if (condominioGenerar && torreGenerar && unidadGenerar) {
        const opcionesOriginalesUnidades = Array.from(unidadGenerar.options).filter(opt => opt.value !== "");
        function actualizarTorresYUnidades() {
            const condominioSeleccionado = condominioGenerar.value;
            
            torreGenerar.innerHTML = '<option value="">Seleccione Torre...</option>';
            unidadGenerar.innerHTML = '<option value="">Seleccione...</option>';

            if (!condominioSeleccionado) {
                torreGenerar.disabled = true;
                unidadGenerar.disabled = true;
                return;
            }

            torreGenerar.disabled = false;
            unidadGenerar.disabled = true;
            const torresUnicas = new Set();
            const unidadesFiltradas = opcionesOriginalesUnidades.filter(opt => opt.getAttribute('data-condominio') === condominioSeleccionado);

            unidadesFiltradas.forEach(opt => {
                const nombreTorre = opt.getAttribute('data-torre');
                if (nombreTorre) {
                    torresUnicas.add(nombreTorre);
                }
            });

            Array.from(torresUnicas).sort().forEach(nombreTorre => {
                const opt = document.createElement('option');
                opt.value = nombreTorre;
                opt.textContent = nombreTorre;
                torreGenerar.appendChild(opt);
            });
        }

        function filtrarUnidadesPorTorre() {
            const condominioSeleccionado = condominioGenerar.value;
            const torreSeleccionada = torreGenerar.value;

            unidadGenerar.innerHTML = '<option value="">Seleccione...</option>';

            if (!torreSeleccionada) {
                unidadGenerar.disabled = true;
                return;
            }

            unidadGenerar.disabled = false;
            const unidadesFiltradas = opcionesOriginalesUnidades.filter(opt => {
                const coincideCondominio = opt.getAttribute('data-condominio') === condominioSeleccionado;
                const coincideTorre = opt.getAttribute('data-torre') === torreSeleccionada;
                return coincideCondominio && coincideTorre;
            });

            unidadesFiltradas.forEach(opt => {
                unidadGenerar.appendChild(opt.cloneNode(true));
            });
        }
        condominioGenerar.addEventListener('change', actualizarTorresYUnidades);
        torreGenerar.addEventListener('change', filtrarUnidadesPorTorre);
        
        actualizarTorresYUnidades();
    }
});
