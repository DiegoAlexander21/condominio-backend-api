document.addEventListener('DOMContentLoaded', function() {
    if (typeof $ !== 'undefined') {
        $('.select2').select2({
            theme: 'classic',
            width: '100%'
        });
    }

    const selectorCondominio = document.getElementById('condominioId');
    const selectorUnidad = document.getElementById('unidadId');
    const selectorArea = document.getElementById('areaComunId');

    if (selectorCondominio) {
        selectorCondominio.addEventListener('change', function() {
            const idCondominio = this.value;
            
            if (selectorUnidad) {
                Array.from(selectorUnidad.options).forEach(opcion => {
                    if (opcion.value === "") return;
                    if (opcion.dataset.condominio === idCondominio || idCondominio === "") {
                        opcion.style.display = '';
                    } else {
                        opcion.style.display = 'none';
                    }
                });
                selectorUnidad.value = "";
            }

            if (selectorArea) {
                Array.from(selectorArea.options).forEach(opcion => {
                    if (opcion.value === "") return;
                    if (opcion.dataset.condominio === idCondominio || idCondominio === "") {
                        opcion.style.display = '';
                    } else {
                        opcion.style.display = 'none';
                    }
                });
                selectorArea.value = "";
            }
        });
    }

    const btnSeleccionarArchivos = document.getElementById('btnSeleccionarArchivos');
    const inputArchivos = document.getElementById('evidenciaArchivos');
    const contenedorVistaPrevia = document.getElementById('evidenciaPreview');
    const formIncidencia = document.querySelector('form');
    const urlEvidencia = document.getElementById('urlEvidencia');

    if (btnSeleccionarArchivos && inputArchivos && formIncidencia) {
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

        formIncidencia.addEventListener('submit', async function(e) {
            if (archivosPendientes.length === 0) {
                return; 
            }

            e.preventDefault();
            const btnSubir = formIncidencia.querySelector('button[type="submit"]');
            const textoOriginal = btnSubir.innerHTML;
            btnSubir.innerHTML = 'Subiendo evidencias...';
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
                formIncidencia.submit();
            } catch (errorSubida) {
                console.error('Error subiendo imágenes:', errorSubida);
                alert('Hubo un error subiendo las imágenes. Por favor, intenta de nuevo.');
                btnSubir.innerHTML = textoOriginal;
                btnSubir.disabled = false;
            }
        });
    }

    const modalIncidencia = document.getElementById('modalIncidencia');
    const botonesDetalles = document.querySelectorAll('.boton-detalles');
    const cerrarModal = document.querySelector('.cerrar-modal');

    if (modalIncidencia && botonesDetalles) {
        botonesDetalles.forEach(boton => {
            boton.addEventListener('click', async function() {
                const id = this.dataset.id;
                document.getElementById('modalCausa').textContent = boton.dataset.causa || 'Sin causa';
                document.getElementById('modalLugar').textContent = boton.dataset.lugar || 'No especificada';
                document.getElementById('modalEstado').textContent = boton.dataset.estado || 'No especificado';
                document.getElementById('modalGravedad').textContent = this.dataset.gravedad;
                document.getElementById('modalFecha').textContent = this.dataset.fecha;
                document.getElementById('modalResponsable').textContent = this.dataset.responsable && this.dataset.responsable.trim() !== '' ? this.dataset.responsable : 'Sin asignar';
                document.getElementById('modalDesc').textContent = boton.dataset.desc || 'Sin descripción';

                const contEvidencias = document.getElementById('modalEvidencias');
                contEvidencias.innerHTML = '<p>Cargando evidencias...</p>';
                modalIncidencia.style.display = 'block';

                try {
                    const response = await fetch('/incidencias/evidencias/' + id);
                    if (!response.ok) throw new Error('Error de red');
                    const evidenciasUrls = await response.json();
                    
                    contEvidencias.innerHTML = '';
                    if (evidenciasUrls.length === 0) {
                        contEvidencias.innerHTML = '<p>No hay evidencias registradas.</p>';
                    } else {
                        evidenciasUrls.forEach(url => {
                            const contenedor = document.createElement('div');
                            contenedor.className = 'contenedor-imagen-previa';
                            const img = document.createElement('img');
                            img.src = url;
                            img.style.cursor = 'pointer';
                            img.onclick = function() { window.open(this.src, '_blank'); };
                            contenedor.appendChild(img);
                            contEvidencias.appendChild(contenedor);
                        });
                    }
                } catch (error) {
                    console.error('Error al cargar evidencias:', error);
                    contEvidencias.innerHTML = '<p>Error al cargar las evidencias.</p>';
                }
            });
        });

        if (cerrarModal) {
            cerrarModal.addEventListener('click', function() {
                modalIncidencia.style.display = 'none';
            });
        }

        window.addEventListener('click', function(e) {
            if (e.target == modalIncidencia) {
                modalIncidencia.style.display = 'none';
            }
        });
    }
});
