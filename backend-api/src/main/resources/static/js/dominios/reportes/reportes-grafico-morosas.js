function inicializarGraficoMorosas() {
  if (!window.datosReportes) {
    return;
  }

  var morosasNombres = window.datosReportes.morosasNombres || [];
  var morosasSaldos = window.datosReportes.morosasSaldos || [];

  if (morosasNombres.length > 0) {
    var opcionesMorosas = {
      chart: {
        type: "bar",
        height: 380,
        toolbar: {
          show: false,
        },
        fontFamily: "Outfit, sans-serif",
      },
      plotOptions: {
        bar: {
          horizontal: false,
          columnWidth: "40%",
          borderRadius: 6,
        },
      },
      dataLabels: {
        enabled: false,
      },
      colors: ["#DC2626"],
      series: [
        {
          name: "Saldo Vencido",
          data: morosasSaldos,
        },
      ],
      xaxis: {
        categories: morosasNombres,
        labels: {
          style: {
            colors: "#333",
            fontSize: "12px",
            fontWeight: 500,
          },
        },
      },
      yaxis: {
        labels: {
          formatter: function (valor) {
            return (
              "S/ " +
              valor.toLocaleString("es-PE", {
                minimumFractionDigits: 0,
                maximumFractionDigits: 0,
              })
            );
          },
          style: {
            colors: "#666",
          },
        },
      },
      grid: {
        borderColor: "#F3F4F6",
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

    var graficoMorosas = new ApexCharts(
      document.querySelector("#graficoMorosas"),
      opcionesMorosas,
    );
    graficoMorosas.render();
  } else {
    document.querySelector("#graficoMorosas").innerHTML =
      '<p class="celda-vacia">No hay deudas morosas registradas actualmente.</p>';
  }
}
