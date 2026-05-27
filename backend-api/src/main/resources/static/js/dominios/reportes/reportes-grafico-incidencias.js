function inicializarGraficoIncidencias() {
  if (!window.datosReportes) {
    return;
  }

  var incidenciasNombres = window.datosReportes.incidenciasNombres || [];
  var incidenciasTotales = window.datosReportes.incidenciasTotales || [];

  if (incidenciasNombres.length > 0) {
    var opcionesIncidencias = {
      chart: {
        type: "bar",
        height: 350,
        toolbar: {
          show: false,
        },
        fontFamily: "Outfit, sans-serif",
      },
      plotOptions: {
        bar: {
          horizontal: true,
          barHeight: "55%",
          borderRadius: 4,
        },
      },
      colors: ["#313852"],
      series: [
        {
          name: "Incidencias",
          data: incidenciasTotales,
        },
      ],
      xaxis: {
        categories: incidenciasNombres,
        labels: {
          style: {
            colors: "#666",
            fontSize: "12px",
          },
        },
      },
      yaxis: {
        labels: {
          style: {
            colors: "#333",
            fontSize: "13px",
            fontWeight: 500,
          },
        },
      },
      grid: {
        borderColor: "#F3F4F6",
        xaxis: {
          lines: {
            show: true,
          },
        },
      },
      tooltip: {
        theme: "light",
        y: {
          formatter: function (valor) {
            return valor === 1 ? valor + " reporte" : valor + " reportes";
          },
        },
      },
    };

    var graficoIncidencias = new ApexCharts(
      document.querySelector("#graficoIncidencias"),
      opcionesIncidencias,
    );
    graficoIncidencias.render();
  } else {
    document.querySelector("#graficoIncidencias").innerHTML =
      '<p class="celda-vacia">No hay reportes de incidencias registrados.</p>';
  }
}
