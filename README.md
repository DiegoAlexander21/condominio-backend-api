1. Planteamiento del Problema
En la actualidad, la administración de condominios y edificios residenciales sufre de una alta informalidad y desorganización. La información vital suele estar fragmentada en grupos de WhatsApp o cuadernos físicos de conserjería, lo que genera problemas de trazabilidad. No existe un control claro sobre la reserva de áreas comunes, y los daños o incidencias rara vez cuentan con un registro adecuado de causas, responsables o evidencias. Como consecuencia, el cálculo y la distribución de gastos extraordinarios carecen de transparencia, lo que genera conflictos vecinales y dificulta el seguimiento de la morosidad.
Asimismo, procesos cotidianos como el ingreso de visitas, la recepción de paquetes y la toma de decisiones comunitarias carecen de herramientas digitales que agilicen la comunicación. A esto se suma una carencia de herramientas para supervisar el cumplimiento de normas de salud y estándares ambientales; el mantenimiento suele ser reactivo y no preventivo, sin un control estricto de los insumos químicos utilizados ni una métrica que evalúe objetivamente el estado de conservación de las áreas compartidas.

2. Descripción y Alcance del Sistema
El proyecto propone una plataforma web centralizada que digitaliza la gestión operativa, financiera, comunicacional y de calidad de un condominio. El sistema administrará el directorio de unidades y residentes, automatizará las reservas de espacios compartidos para prevenir conflictos de horario, y contará con un motor robusto para el registro de incidencias categorizadas por gravedad y causa. Incluirá un módulo financiero dinámico capaz de separar cuotas de mantenimiento fijas de gastos extraordinarios, distribuyendo los costos automáticamente por diversos métodos y manteniendo un estado de cuenta actualizado por unidad.
A nivel de gestión avanzada, el sistema evolucionará para ser una herramienta de gestión de calidad. Permitirá supervisar el estado ambiental de las áreas mediante listas de verificación (checklists) de cumplimiento sanitario, controlar el uso de insumos de limpieza (como lejía y otros químicos) para garantizar estándares de salud y calificar el desempeño del mantenimiento mediante un sistema de puntajes dinámico. Se integran además herramientas para el control de accesos, notificación de paquetería y asambleas virtuales apoyadas por Inteligencia Artificial y actualizaciones en tiempo real.


3. Estructura de Módulos (Requerimientos Funcionales)
Módulo 1: Seguridad y Autenticación
•	RF1: El sistema debe permitir la autenticación de usuarios mediante correo y contraseña, generando un token de sesión seguro (JWT).
•	RF2: El sistema debe gestionar accesos basados en roles: Administrador, Propietario, Residente y Conserjería/Mantenimiento.

Módulo 2: Gestión de Condominio y Unidades 
•	RF3: El Administrador debe poder registrar y gestionar los datos del condominio (torres, pisos).
•	RF4: El sistema debe permitir el registro de departamentos/unidades, asignando propietarios y residentes activos.
•	RF5: El sistema debe mantener un historial de titularidad y cambios de dueño por unidad.

Módulo 3: Áreas Comunes y Reservas
•	RF6: El Administrador debe poder crear áreas comunes (piscina, parrilla, SUM) definiendo capacidad, horarios y normas de uso.
•	RF7: Los Residentes deben poder solicitar reservas de áreas comunes seleccionando fecha y bloque horario.
•	RF8: El sistema debe validar automáticamente la disponibilidad para evitar cruces de horarios y registrar al usuario responsable.

Módulo 4: Registro de Incidencias y Daños
•	RF9: Los usuarios deben poder reportar una incidencia indicando área afectada, fecha/hora y evidencia fotográfica.
•	RF10: El sistema debe categorizar la incidencia por gravedad (leve, moderado, grave, crítico) y causa (desgaste, mal uso, clima, vandalismo).
•	RF11: El Administrador debe poder actualizar el estado de atención y asignar un responsable si el daño fue provocado.

Módulo 5: Finanzas y Distribución de Gastos (Lógica de Negocio)
•	RF12: El sistema debe diferenciar gastos fijos (mantenimiento regular) y extraordinarios (reparaciones por incidencias).
•	RF13: El sistema debe calcular la distribución de gastos extraordinarios (partes iguales, coeficiente por tamaño o cobro directo).
•	RF14: El sistema debe generar el estado de cuenta mensual por unidad, sumando cuotas fijas y deudas extraordinarias.
•	RF15: El Administrador debe poder registrar los pagos parciales o totales y visualizar el índice de morosidad.

Módulo 6: Reportes y Dashboard
•	RF16: El sistema debe proveer paneles gráficos que muestren incidencias frecuentes, áreas con mayor gasto y unidades morosas.

Módulo 7: Control de Accesos y Visitas
•	RF17: El Residente debe poder pre-registrar visitas esperadas, generando un registro temporal.
•	RF18: La Conserjería debe poder registrar el ingreso y salida real de las visitas, validando contra los pre-registros.

Módulo 8: Gestión de Paquetería y Correspondencia (Integración API)
•	RF19: La Conserjería debe poder registrar la recepción de un paquete para una unidad específica.
•	RF20: El sistema debe enviar automáticamente una notificación externa (vía API de Email/WhatsApp) al residente receptor.
•	RF21: El sistema debe registrar la entrega final del paquete con fecha y hora.

Módulo 9: Comunicados y Asambleas Virtuales (Innovación IA y WebSockets)
•	RF22: El Administrador debe poder generar comunicados asistidos por una API de IA (LLM), transformando borradores en textos formales.
•	RF23: El Administrador debe poder abrir sesiones de votación para asambleas virtuales con opciones definidas.
•	RF24: Los resultados de las votaciones deben actualizarse para todos los usuarios en tiempo real mediante WebSockets.

Módulo 10: Gestión Ambiental y Salud
•	RF25: El sistema debe permitir la creación de Listas de Verificación (Checklists) de Salud y Ambiente por área (ej. niveles de cloro, gestión de residuos).
•	RF26: El sistema debe evaluar el cumplimiento de normas sanitarias mediante ítems de "pasa/no pasa", generando alertas si un área no es apta.
•	RF27: El sistema debe registrar la huella de mantenimiento, permitiendo auditorías sobre la frecuencia de desinfección en zonas críticas.

Módulo 11: Control de Insumos y Mantenimiento Técnico
•	RF28: El sistema debe permitir el registro y control de ítems de mantenimiento (ej. cantidad de lejía, filtros, desinfectantes) asociados a una tarea específica.
•	RF29: El sistema debe calcular el costo de los insumos utilizados para su posterior distribución en el módulo financiero.
•	RF30: El personal debe poder marcar el stock utilizado para prever la reposición de suministros esenciales.

Módulo 12: Sistema de Calificación y Estado de Áreas
•	RF31: El sistema debe asignar una Calificación de Estado (Rating) automática basada en el historial de incidencias y checklists ambientales.
•	RF32: Los Residentes podrán calificar el estado de limpieza y funcionalidad de un área tras su uso.
•	RF33: El Dashboard debe mostrar un "Ranking de Áreas" para priorizar inversiones en las zonas con mayor desgaste o riesgo sanitario.
