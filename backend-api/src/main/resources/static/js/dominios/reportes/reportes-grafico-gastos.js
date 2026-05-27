function inicializarGraficoGastos() {
  if (!window.datosReportes) {
    return;
  }

  var gastosNombres = window.datosReportes.gastosNombres || [];
  var gastosMontos = window.datosReportes.gastosMontos || [];

  if (gastosNombres.length > 0) {
    var opcionesGastos = {
      chart: {
        type: "donut",
        height: 350,
        fontFamily: "Outfit, sans-serif",
      },
      labels: gastosNombres,
      series: gastosMontos,
      colors: ["#313852", "#C2CBD4", "#0D9488", "#DC2626", "#F59E0B"],
      stroke: {
        show: true,
        width: 2,
        colors: ["#ffffff"],
      },
      plotOptions: {
        pie: {
          donut: {
            size: "65%",
          },
        },
      },
      legend: {
        position: "bottom",
        fontSize: "13px",
        markers: {
          radius: 12,
        },
      },
      tooltip: {
        theme: "light",
        y: {
          formatter: function (valor) {
            return (
              "S/ " +
              valor.toLocaleString("es-PE", {
                minimumFractionDigits: 2,
                maximumFractionDigits: 2,
              })
            );
          },
        },
      },
    };

    var graficoGastos = new ApexCharts(
      document.querySelector("#graficoGastos"),
      opcionesGastos,
    );
    graficoGastos.render();
  } else {
    document.querySelector("#graficoGastos").innerHTML =
      '<p class="celda-vacia">No se registran gastos de mantenimiento en areas comunes.</p>';
  }
}
