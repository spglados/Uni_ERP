window.onload = function() {
  const currentDate = new Date();
  const formattedDate = formatDate(currentDate) + " 일 통계입니다";
  document.getElementById("currentDate").innerHTML = formattedDate;
};

function formatDate(date) {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
}

let salesHistory = [];
let itemCountHistory = [];
let itemProfitHistory = [];
let chart = null;
let itemCountChart = null;
let itemProfitChart = null;

function updateCharts() {
  const ctx1 = document.getElementById('sales-chart').getContext('2d');
  const ctx2 = document.getElementById('item-count-chart').getContext('2d');
  const ctx3 = document.getElementById('item-profit-chart').getContext('2d');

  const salesPrices = [];
  const itemName = itemProfitHistory.map(item => item.itemName);
  const itemCountValues = itemProfitHistory.map(item => item.quantity);
  const itemProfitValues = itemProfitHistory.map(item => item.quantity * item.unitPrice);
  const hours = Array.from({length: 24}, (_, i) => `${i.toString().padStart(2, '0')}:00`);

  console.log(salesHistory);
  console.log(itemName);
  console.log(itemCountValues);
  console.log(itemProfitValues);

  salesHistory.forEach(function(sale) {
    const existingIndex1 = salesPrices.findIndex(date => date === sale.salesDate);
    if (existingIndex1 !== -1) {
      salesPrices[existingIndex1] += sale.totalPrice;
    } else {
      salesPrices.push(sale.salesDate);
      salesPrices.push(sale.totalPrice);
    }

    const existingIndex2 = itemName.findIndex(item => item === sale.itemName);
    if (sale.itemName !== undefined && sale.itemName !== null) {
      const existingIndex2 = itemName.findIndex(item => item === sale.itemName);
      if (existingIndex2 !== -1) {
        itemCountValues[existingIndex2] += sale.quantity;
        itemProfitValues[existingIndex2] += sale.unitPrice * sale.quantity;
      } else {
        itemName.push(sale.itemName);
        itemCountValues.push(sale.quantity);
        itemProfitValues.push(sale.unitPrice * sale.quantity);
      }
    }
  });


  if (chart) {
    chart.destroy();
  }
  chart = new Chart(ctx1, {
    type: 'line',
    data: {
      labels: hours,
      datasets: [{
        label: '총 매출',
        data: hours.map(hour => {
          const sales = salesHistory.filter(s => new Date(s.salesDate).getHours() === parseInt(hour.split(':')[0]));
          return sales.reduce((acc, sale) => acc + sale.totalPrice, 0);
        }),
        backgroundColor: 'rgba(255, 99, 132, 0.2)',
        borderColor: 'rgba(255, 99, 132, 1)',
        borderWidth: 1
      }]
    },
    options: {
      responsive: false,
      plugins: {
        title: {
          display: true,
          text: '매출 차트'
        }
      },
      scales: {
        x: { // x 축 설정
          type: 'category', // 기본 설정
          ticks: {
            autoSkip: false, // display all ticks
            maxRotation: 0, // prevent rotation of tick labels
            font: {
              size: 10 // adjust font size to fit all labels
            }
          }
        },
        y: { // y 축 설정
          beginAtZero: false, // y축 값이 0부터 시작하게 설정
          ticks: {
            stepSize: 10000 // y축 값의 간격 설정
          }
        }
      }
    }
  });

  if (itemCountChart) {
    itemCountChart.destroy();
  }
  itemCountChart = new Chart(ctx2, {
    type: 'bar',
    data: {
      labels: itemName,
      datasets: [{
        label: '품목별 판매량',
        data: itemCountValues,
        backgroundColor: [
                  'rgba(255, 99, 132, 0.2)',
                  'rgba(54, 162, 235, 0.2)',
                  'rgba(255, 206, 86, 0.2)'
                ],
                borderColor: [
                  'rgba(255, 99, 132, 1)',
                  'rgba(54, 162, 235, 1)',
                  'rgba(255, 206, 86, 1)'
                ],
        borderWidth: 1
      }]
    },
    options: {
      responsive: false,
      plugins: {
        title: {
          display: true,
          text: '품목 별 매출 비율'
        }
      },
      scales: {
        x: { // x 축 설정
          type: 'category', // 기본 설정
        },
        y: { // y 축 설정
          beginAtZero: true, // y축 값이 0부터 시작하게 설정
          ticks: {
            stepSize: 10 // y축 값의 간격 설정
          }
        }
      }
    }
  });

  if (itemProfitChart) {
    itemProfitChart.destroy();
  }
  itemProfitChart = new Chart(ctx3, {
    type: 'pie',
    data: {
      labels: itemName,
      datasets: [{
        label: 'Profit Per Item',
        data: itemProfitValues,
        backgroundColor: [
          'rgba(255, 99, 132, 0.2)',
          'rgba(54, 162, 235, 0.2)',
          'rgba(255, 206, 86, 0.2)'
        ],
        borderColor: [
          'rgba(255, 99, 132, 1)',
          'rgba(54, 162, 235, 1)',
          'rgba(255, 206, 86, 1)'
        ],
        borderWidth: 1
      }]
    },
    options: {
      responsive: false,
      plugins: {
        title: {
          display: true,
          text: '매출 중 품목당 비율'
        }
      }
    }
  });
}

$.when(
  $.ajax({
    type: "GET",
    url: "/erp/sales/data",
    success: function(data) {
      salesHistory = data;
    }
  }),
  $.ajax({
    type: "GET",
    url: "/erp/sales/itemCount",
    success: function(data) {
      itemCountHistory = data;
    }
  }),
  $.ajax({
    type: "GET",
    url: "/erp/sales/itemProfit",
    success: function(data) {
      itemProfitHistory = data;
    }
  })
).then(function() {
  updateCharts();
});
