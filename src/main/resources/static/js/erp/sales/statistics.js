window.onload = function() {
  const currentDate = new Date();
  const formattedDate = formatDate(currentDate) + " 일 통계입니다";
  document.getElementById("currentDate").innerHTML = formattedDate;

  // Apply CSS styles to percentage elements
  const percentageElements = document.querySelectorAll('.percentage');
  percentageElements.forEach((element) => {
    const percentage = parseFloat(element.textContent.replace('%', '').replace('(', '').replace(')', ''));
    if (percentage > 0) {
      element.style.color = 'green';
      element.textContent = `(+${percentage}%)`;
    } else if (percentage < 0) {
      element.style.color = 'red';
      element.textContent = `(${percentage}%)`;
    }
  });
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
      indexAxis: 'y',
      scales: {
        x: { // x 축 설정
          beginAtZero: false, // 0부터 시작 설정
        },
        y: { // y 축 설정
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

Promise.all([
  fetch("/erp/sales/data").then(response => response.json()),
  fetch("/erp/sales/itemCount").then(response => response.json()),
  fetch("/erp/sales/itemProfit").then(response => response.json())
]).then(([salesData, itemCountData, itemProfitData]) => {
  salesHistory = salesData;
  itemCountHistory = itemCountData;
  itemProfitHistory = itemProfitData;
  updateCharts();
});

let sortOrders = {};
let selectedColumn = null;

function sortTable(column) {
  const table = document.querySelector('.table');
  const rows = table.rows;
  const sortedRows = Array.from(rows).slice(1); // skip the header row

  // remove the selected class from all columns
  const headerRow = table.rows[0];
  for (let i = 0; i < headerRow.cells.length; i++) {
    headerRow.cells[i].classList.remove('selected');
    headerRow.cells[i].textContent = headerRow.cells[i].textContent.replace('▲', '').replace('▼', '');
  }

  // remove the selected class from all child rows
  for (let i = 0; i < sortedRows.length; i++) {
    for (let j = 0; j < sortedRows[i].cells.length; j++) {
      sortedRows[i].cells[j].classList.remove('selected');
    }
  }

  // add the selected class to the current column
  headerRow.cells[column].classList.add('selected');
  selectedColumn = column;

  // add the ▲ or ▼ symbol to the selected column header
  if (sortOrders[column] === 'ASC') {
    headerRow.cells[column].textContent += ' ▼';
  } else if (sortOrders[column] === 'DESC') {
    headerRow.cells[column].textContent += ' ▲';
  } else {
      headerRow.cells[column].textContent += ' ▼';
  }

  // add the selected class to the child rows
  for (let i = 0; i < sortedRows.length; i++) {
    sortedRows[i].cells[column].classList.add('selected');
  }

  // check if the column is already sorted, and if so, toggle the sorting order
  if (sortOrders[column] === 'DESC') {
    sortOrders[column] = 'ASC';
  } else {
    sortOrders[column] = 'DESC';
  }

  sortedRows.sort((a, b) => {
    const aValue = a.cells[column].textContent;
    const bValue = b.cells[column].textContent;

    // convert the values to the appropriate data type
    let aNum, bNum;
    if (column === 1 || column === 2) { // assuming columns 1 and 2 are the percentage columns
      aNum = parseFloat(aValue.replace('%', '')); // extract the percentage value
      bNum = parseFloat(bValue.replace('%', ''));
    } else {
      aNum = parseFloat(aValue);
      bNum = parseFloat(bValue);
    }

    // compare the values based on the current sorting order
    if (sortOrders[column] === 'DESC') {
      if (aNum > bNum) {
        return -1;
      } else if (aNum < bNum) {
        return 1;
      } else {
        // if the values are the same, compare them as strings
        if (aValue > bValue) {
          return -1;
        } else if (aValue < bValue) {
          return 1;
        } else {
          return 0;
        }
      }
    } else {
      if (aNum < bNum) {
        return -1;
      } else if (aNum > bNum) {
        return 1;
      } else {
        // if the values are the same, compare them as strings
        if (aValue < bValue) {
          return -1;
        } else if (aValue > bValue) {
          return 1;
        } else {
          return 0;
        }
      }
    }
  });

  // append the sorted rows back to the table
  sortedRows.forEach((row) => {
    table.appendChild(row);
  });
}

document.querySelectorAll('table th').forEach((th, index) => {
  th.addEventListener('click', () => {
    sortTable(index);
  });
});
