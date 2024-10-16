window.onload = function() {
  const startDateInput = document.getElementById('startDate');
  const endDateInput = document.getElementById('endDate');
  const currentDate = new Date();
  const oneMonthAgo = new Date(currentDate.getTime() - 30 * 24 * 60 * 60 * 1000);

  if (startDateInput && endDateInput) {
    startDateInput.value = oneMonthAgo.toISOString().slice(0, 16);
    endDateInput.value = currentDate.toISOString().slice(0, 16);
  }

};

var salesHistory = [];
var itemCountHistory = [];
var itemProfitHistory = [];
var chart = null;
var itemCountChart = null;
var itemProfitChart = null;

function updateCharts(chartType) {
  const ctx1 = document.getElementById('sales-chart').getContext('2d');
  const ctx2 = document.getElementById('item-count-chart').getContext('2d');
  const ctx3 = document.getElementById('item-profit-chart').getContext('2d');

    console.log('salesHistory:', salesHistory);
    console.log('itemCountHistory:', itemCountHistory);
    console.log('itemProfitHistory:', itemProfitHistory);

  const salesDates = [];
  const salesPrices = [];
  const itemCountValues = [];
  const itemProfitValues = [];

  salesHistory.forEach(function(sale) {
    var date = new Date(sale.salesDate);
    var formattedDate;

    switch (chartType) {
      case 'hourly':
        formattedDate = date.toLocaleTimeString(); // Get the hour only
        break;
      case 'daily':
        formattedDate = date.toLocaleDateString(); // Get the day only
        break;
      case 'monthly':
        formattedDate = date.toLocaleString('default', { month: 'long', year: 'numeric' }); // Get the month and year
        break;
      case 'yearly':
        formattedDate = date.getFullYear(); // Get the year only
        break;
    }

    const existingIndex = salesDates.findIndex(date => date === formattedDate);
    if (existingIndex !== -1) {
      salesPrices[existingIndex] += sale.totalPrice;
      itemCountValues[existingIndex] += sale.itemCount;
      itemProfitValues[existingIndex] += sale.itemProfit;
    } else {
      salesDates.push(formattedDate);
      salesPrices.push(sale.totalPrice);
      itemCountValues.push(sale.itemCount);
      itemProfitValues.push(sale.itemProfit);
    }
  });

  if (chart) {
    chart.destroy();
  }
  chart = new Chart(ctx1, {
    type: 'line',
    data: {
      labels: salesDates,
      datasets: [{
        label: 'Total Price',
        data: salesPrices,
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
      labels: itemCountHistory.map(function(item) {
        return new Date(item.date).toLocaleDateString();
      }),
      datasets: [{
        label: 'Item Count',
        data: itemCountHistory.map(function(item) {
          return item.count;
        }),
        backgroundColor: 'rgba(54, 162, 235, 0.2)',
        borderColor: 'rgba(54, 162, 235, 1)',
        borderWidth: 1
      }]
    },
    options: {
      responsive: false,
      plugins: {
        title: {
          display: true,
          text: '품목 수 차트'
        }
      },
      scales: {
        x: { // x 축 설정
          type: 'category', // 기본 설정
        },
        y: { // y 축 설정
          beginAtZero: false, // y축 값이 0부터 시작하게 설정
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
      labels: itemProfitHistory.map(function(item) {
        return new Date(item.date).toLocaleDateString();
      }),
      datasets: [{
        label: 'Profit Per Item',
        data: itemProfitHistory.map(function(item) {
          return item.profit;
        }),
        backgroundColor: 'rgba(54, 162, 235, 0.2)',
        borderColor: 'rgba(54, 162, 235, 1)',
        borderWidth: 1
      }]
    },
    options: {
      responsive: false,
      plugins: {
        title: {
          display: true,
          text: '품목 수 차트'
        }
      },
      scales: {
        x: { // x 축 설정
          type: 'category', // 기본 설정
        },
        y: { // y 축 설정
          beginAtZero: false, // y축 값이 0부터 시작하게 설정
          ticks: {
            stepSize: 10 // y축 값의 간격 설정
          }
        }
      }
    }
  });
}

$.ajax({
  type: "GET",
  url: "/erp/sales/data",
  success: function(data) {
    salesHistory = data;
    updateCharts('hourly');
  }
});

$.ajax({
  type: "GET",
  url: "/erp/sales/itemCount",
  success: function(data) {
    itemCountHistory = data;
    updateCharts('hourly');
  }
});

$.ajax({
  type: "GET",
  url: "/erp/sales/itemProfit",
  success: function(data) {
    itemProfitHistory = data;
    updateCharts('hourly');
  }
});


function submitDates() {
  var startDate = document.getElementById('startDate').value;
  var endDate = document.getElementById('endDate').value;

  jQuery.ajax({
    type: "GET",
    url: "/erp/sales/data",
    data: {
      startDate: startDate,
      endDate: endDate
    },
    success: function(data) {
      salesHistory = data;
      updateCharts('hourly');
    }
  });

  jQuery.ajax({
    type: "GET",
    url: "/erp/sales/itemCount",
    data: {
      startDate: startDate,
      endDate: endDate
    },
    success: function(data) {
      itemCountHistory = data;
      updateCharts('hourly');
    }
  });

  jQuery.ajax({
    type: "GET",
    url: "/erp/sales/itemProfit",
    data: {
      startDate: startDate,
      endDate: endDate
    },
    success: function(data) {
      itemProfitHistory = data;
      updateCharts('hourly');
    }

  });
}

$(document).ready(function() {
  $('#confirm-button').on('click', submitDates);
});