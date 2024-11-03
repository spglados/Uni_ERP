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
  const itemName = itemProfitHistory.map(item => item.itemName);
  const itemCountValues = itemProfitHistory.map(item => item.quantity);
  const itemProfitValues = itemProfitHistory.map(item => item.quantity * item.unitPrice);

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

    const existingIndex1 = salesDates.findIndex(date => date === formattedDate);
    if (existingIndex1 !== -1) {
      salesPrices[existingIndex1] += sale.totalPrice;
    } else {
      salesDates.push(formattedDate);
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
      labels: salesDates,
      datasets: [{
        label: '총 매출',
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

function updateSalesChart(chartType) {
  const ctx1 = document.getElementById('sales-chart').getContext('2d');

  console.log('salesHistory:', salesHistory);

  const salesDates = [];
  const salesPrices = [];

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
    } else {
      salesDates.push(formattedDate);
      salesPrices.push(sale.totalPrice);
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
        label: 'Sales',
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
}

function submitDates() {
  var startDate = document.getElementById('startDate').value;
  var endDate = document.getElementById('endDate').value;
  var storeId = document.getElementById('storeId').value;

  var data = {
    startDate: startDate,
    endDate: endDate
  };

  if (storeId !== '') {
    data.storeId = storeId;
  }

  jQuery.ajax({
    type: "GET",
    url: "/erp/sales/data",
    data: data,
    success: function(data) {
      salesHistory = data;
    }
  });

  jQuery.ajax({
    type: "GET",
    url: "/erp/sales/itemCount",
    data: data,
    success: function(data) {
      itemCountHistory = data;
    }
  });

  jQuery.ajax({
    type: "GET",
    url: "/erp/sales/itemProfit",
    data: data,
    success: function(data) {
      itemProfitHistory = data;
      updateCharts('hourly');
    }
  });
}

$(document).ready(function() {
  $('#confirm-button').on('click', submitDates);
});