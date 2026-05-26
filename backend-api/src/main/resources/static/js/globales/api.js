const urlBase = '/api/v1';

async function peticionAutenticada(ruta, opciones = {}) {
    const cabeceras = {
        'Content-Type': 'application/json',
        ...opciones.headers
    };

    const configuracion = {
        ...opciones,
        headers: cabeceras,
        credentials: 'same-origin'
    };

    try {
        const respuesta = await fetch(`${urlBase}${ruta}`, configuracion);
        
        if (respuesta.status === 401 || respuesta.status === 403) {
            window.location.href = '/auth/login';
            throw new Error('SesionExpirada');
        }

        if (!respuesta.ok) {
            const error = await respuesta.json();
            throw error;
        }

        if (respuesta.status === 204) {
            return null;
        }

        return await respuesta.json();
    } catch (error) {
        throw error;
    }
}
