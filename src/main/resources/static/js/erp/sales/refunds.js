function updateRefundChart(refundData) {
  const refundChartCtx = document.getElementById('refund-count-chart').getContext('2d');

  const refundItemNames = refundData.map(item => item.itemName);
  const refundItemCounts = refundData.map(item => item.quantity);

  const refundChart = new Chart(refundChartCtx, {
    type: 'bar',
    data: {
      labels: refundItemNames,
      datasets: [{
        label: '수량',
        data: refundItemCounts,
        backgroundColor: [
                  'rgba(240, 145, 217, 0.2)',
                  'rgba(130, 224, 170, 0.2)',
                  'rgba(245, 105, 65, 0.2)',
                  'rgba(75, 191, 255, 0.2)',
                  'rgba(255, 215, 0, 0.2)',
                  'rgba(150, 75, 200, 0.2)',
                  'rgba(220, 150, 50, 0.2)',
                  'rgba(100, 255, 150, 0.2)',
                  'rgba(200, 50, 150, 0.2)',
                  'rgba(50, 200, 255, 0.2)'
                ],
                borderColor: [
                  'rgba(240, 145, 217, 1)',
                  'rgba(130, 224, 170, 1)',
                  'rgba(245, 105, 65, 1)',
                  'rgba(75, 191, 255, 1)',
                  'rgba(255, 215, 0, 1)',
                  'rgba(150, 75, 200, 1)',
                  'rgba(220, 150, 50, 1)',
                  'rgba(100, 255, 150, 1)',
                  'rgba(200, 50, 150, 1)',
                  'rgba(50, 200, 255, 1)'
                ],
        borderWidth: 1
      }]
    },
    options: {
      responsive: false,
      plugins: {
        title: {
          display: true,
          text: '품목별 환불'
        }
      },
      indexAxis: 'y',
      scales: {
        x: {
          ticks: {
            stepSize: 1
          },
          beginAtZero: false
        },
        y: {
          labels: refundItemNames
        }
      }
    }
  });
}

function fetchRefundData() {
  return fetch("/erp/sales/refund-data")
    .then(response => response.json());
}

document.addEventListener("DOMContentLoaded", function() {
  fetchRefundData()
    .then(refundData => updateRefundChart(refundData));
});