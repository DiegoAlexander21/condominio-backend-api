document.addEventListener("DOMContentLoaded", function () {
    const elementosIncidencias = document.querySelectorAll(".datos-incidencia");
    const incidenciasNombres = [];
    const incidenciasTotales = [];
    
    elementosIncidencias.forEach(elemento => {
        incidenciasNombres.push(elemento.getAttribute("data-area"));
        incidenciasTotales.push(parseInt(elemento.getAttribute("data-total")) || 0);
    });

    const elementosGastos = document.querySelectorAll(".datos-gasto");
    const gastosNombres = [];
    const gastosMontos = [];
    
    elementosGastos.forEach(elemento => {
        gastosNombres.push(elemento.getAttribute("data-area"));
        gastosMontos.push(parseFloat(elemento.getAttribute("data-monto")) || 0.0);
    });

    const elementosMorosas = document.querySelectorAll(".datos-morosa");
    const morosasNombres = [];
    const morosasSaldos = [];
    
    elementosMorosas.forEach(elemento => {
        morosasNombres.push(elemento.getAttribute("data-unidad"));
        morosasSaldos.push(parseFloat(elemento.getAttribute("data-saldo")) || 0.0);
    });

    if (incidenciasNombres.length > 0) {
        const opcionesIncidencias = {
            chart: {
                type: "bar",
                height: 350,
                toolbar: {
                    show: false
                },
                fontFamily: "Outfit, sans-serif"
            },
            plotOptions: {
                bar: {
                    horizontal: true,
                    barHeight: "55%",
                    borderRadius: 4
                }
            },
            colors: ["#313852"],
            series: [{
                name: "Incidencias",
                data: incidenciasTotales
            }],
            xaxis: {
                categories: incidenciasNombres,
                labels: {
                    style: {
                        colors: "#666",
                        fontSize: "12px"
                    }
                }
            },
            yaxis: {
                labels: {
                    style: {
                        colors: "#333",
                        fontSize: "13px",
                        fontWeight: 500
                    }
                }
            },
            grid: {
                borderColor: "#F3F4F6",
                xaxis: {
                    lines: {
                        show: true
                    }
                }
            },
            tooltip: {
                theme: "light",
                y: {
                    formatter: function (valor) {
                        return valor === 1 ? valor + " reporte" : valor + " reportes";
                    }
                }
            }
        };

        const graficoIncidencias = new ApexCharts(document.querySelector("#graficoIncidencias"), opcionesIncidencias);
        graficoIncidencias.render();
    } else {
        document.querySelector("#graficoIncidencias").innerHTML = '<p class="celda-vacia">No hay reportes de incidencias registrados.</p>';
    }

    if (gastosNombres.length > 0) {
        const opcionesGastos = {
            chart: {
                type: "donut",
                height: 350,
                fontFamily: "Outfit, sans-serif"
            },
            labels: gastosNombres,
            series: gastosMontos,
            colors: ["#313852", "#C2CBD4", "#0D9488", "#DC2626", "#F59E0B"],
            stroke: {
                show: true,
                width: 2,
                colors: ["#ffffff"]
            },
            plotOptions: {
                pie: {
                    donut: {
                        size: "65%"
                    }
                }
            },
            legend: {
                position: "bottom",
                fontSize: "13px",
                markers: {
                    radius: 12
                }
            },
            tooltip: {
                theme: "light",
                y: {
                    formatter: function (valor) {
                        return "S/ " + valor.toLocaleString("es-PE", { minimumFractionDigits: 2, maximumFractionDigits: 2 });
                    }
                }
            }
        };

        const graficoGastos = new ApexCharts(document.querySelector("#graficoGastos"), opcionesGastos);
        graficoGastos.render();
    } else {
        document.querySelector("#graficoGastos").innerHTML = '<p class="celda-vacia">No se registran gastos de mantenimiento en áreas comunes.</p>';
    }

    if (morosasNombres.length > 0) {
        const opcionesMorosas = {
            chart: {
                type: "bar",
                height: 380,
                toolbar: {
                    show: false
                },
                fontFamily: "Outfit, sans-serif"
            },
            plotOptions: {
                bar: {
                    horizontal: false,
                    columnWidth: "40%",
                    borderRadius: 6
                }
            },
            dataLabels: {
                enabled: false
            },
            colors: ["#DC2626"],
            series: [{
                name: "Saldo Vencido",
                data: morosasSaldos
            }],
            xaxis: {
                categories: morosasNombres,
                labels: {
                    style: {
                        colors: "#333",
                        fontSize: "12px",
                        fontWeight: 500
                    }
                }
            },
            yaxis: {
                labels: {
                    formatter: function (valor) {
                        return "S/ " + valor.toLocaleString("es-PE", { minimumFractionDigits: 0, maximumFractionDigits: 0 });
                    },
                    style: {
                        colors: "#666"
                    }
                }
            },
            grid: {
                borderColor: "#F3F4F6"
            },
            tooltip: {
                theme: "light",
                y: {
                    formatter: function (valor) {
                        return "S/ " + valor.toLocaleString("es-PE", { minimumFractionDigits: 2, maximumFractionDigits: 2 });
                    }
                }
            }
        };

        const graficoMorosas = new ApexCharts(document.querySelector("#graficoMorosas"), opcionesMorosas);
        graficoMorosas.render();
    } else {
        document.querySelector("#graficoMorosas").innerHTML = '<p class="celda-vacia">No hay deudas morosas registradas actualmente.</p>';
    }
});
