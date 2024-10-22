document.querySelectorAll('table th').forEach((th, index) => {
  th.addEventListener('click', () => {
    sortTable(index);
  });
});

// Function to update chart
function updateChart() {
    const currentDate = new Date();
    const year = currentDate.getFullYear();
    const month = currentDate.getMonth() + 1;

    fetch('/erp/sales/details?year=' + year + '&month=' + month)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            // Update chart data here
            // ...
        })
        .catch(error => console.error('There has been a problem with your fetch operation:', error));
}

document.addEventListener('DOMContentLoaded', function() {
    const submitButton = document.getElementById('submit-btn');
    const yearSelect = document.getElementById('selectYear');
    const monthSelect = document.getElementById('selectMonth');
    const tbody = document.querySelector('tbody');
    const dateSpan = document.getElementById('dateSpan');

    // Set default value to current year-month
    const currentDate = new Date();
    yearSelect.value = currentDate.getFullYear();
    monthSelect.value = currentDate.getMonth() + 1;
    dateSpan.textContent = yearSelect.value + '년 ' + monthSelect.value + '월';

    const fetchSalesData = () => {
        const year = yearSelect.value;
        const month = monthSelect.value;

        fetch('/erp/sales/details?year=' + year + '&month=' + month)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                // Clear the table body
                tbody.innerHTML = '';

                // Populate the table with the sales data
                data.forEach((item) => {
                  const row = document.createElement('tr');
                  row.innerHTML = '<td>' + item.productName + '</td>' +
                    '<td>' + item.monthlySales + '</td>' +
                    '<td>' + (item.lastMonthSales !== null ? item.lastMonthSales : '') + '</td>' +
                    '<td>' + (item.monthlyGrowthRate !== null ? item.monthlyGrowthRate + '%' : '') + '</td>' +
                    '<td>' + item.yearlySales + '</td>' +
                    '<td>' + (item.lastYearSales !== null ? item.lastYearSales : '') + '</td>' +
                    '<td>' + (item.yearlyGrowthRate !== null ? item.yearlyGrowthRate + '%' : '') + '</td>' +
                    '<td>' + item.profit + '</td>' +
                    '<td></td>';

                  // Set the color of the percent values
                  if (item.monthlyGrowthRate !== null) {
                    const monthlyGrowthRateCell = row.cells[3];
                    monthlyGrowthRateCell.style.color = (item.monthlyGrowthRate > 0) ? 'green' : (item.monthlyGrowthRate < 0) ? 'red' : '';
                  }

                  if (item.yearlyGrowthRate !== null) {
                    const yearlyGrowthRateCell = row.cells[6];
                    yearlyGrowthRateCell.style.color = (item.yearlyGrowthRate > 0) ? 'green' : (item.yearlyGrowthRate < 0) ? 'red' : '';
                  }

                  tbody.appendChild(row);
                });

                // Update the sales summary spans
                const salesMonthSpan = document.querySelector('.sales-month-span');
                const salesMonthPercentSpan = document.querySelector('.sales-month-percent-span');
                const salesYearSpan = document.querySelector('.sales-year-span');
                const salesYearPercentSpan = document.querySelector('.sales-year-percent-span');
                const salesProfitSpan = document.querySelector('.sales-profit-span');
                const salesProfitPercentSpan = document.querySelector('.sales-profit-percent-span');
                const targetAchievementRateSpan = document.querySelector('.target-achievement-rate-span');

                // Calculate the total sales for the month and year
                const totalMonthlySales = data.reduce((acc, item) => acc + item.monthlySales, 0);
                const totalYearlySales = data.reduce((acc, item) => acc + item.yearlySales, 0);

                // Calculate the total profit for the year
                const totalProfit = data.reduce((acc, item) => acc + item.profit, 0);

                // Calculate the target achievement rate
                const totalLastYearSales = data.reduce((acc, item) => acc + item.lastYearSales, 0);
                const targetSales = totalLastYearSales * 1.05;
                const targetAchievementRate = (totalYearlySales / targetSales) * 100;

                // Update the sales summary spans
                salesMonthSpan.textContent = totalMonthlySales;
                salesMonthPercentSpan.textContent = (data[0].monthlyGrowthRate !== null ? data[0].monthlyGrowthRate.toFixed(2) + '%' : '');
                salesMonthPercentSpan.style.color = (data[0].monthlyGrowthRate !== null && data[0].monthlyGrowthRate > 0) ? 'green' : (data[0].monthlyGrowthRate !== null && data[0].monthlyGrowthRate < 0) ? 'red' : '';

                salesYearSpan.textContent = totalYearlySales;
                salesYearPercentSpan.textContent = (data[0].yearlyGrowthRate !== null ? data[0].yearlyGrowthRate.toFixed(2) + '%' : '');
                salesYearPercentSpan.style.color = (data[0].yearlyGrowthRate !== null && data[0].yearlyGrowthRate > 0) ? 'green' : (data[0].yearlyGrowthRate !== null && data[0].yearlyGrowthRate < 0) ? 'red' : '';

                salesProfitSpan.textContent = totalProfit;
                salesProfitPercentSpan.textContent = (data[0].yearlyGrowthRate !== null ? data[0].yearlyGrowthRate.toFixed(2) + '%' : '');
                salesProfitPercentSpan.style.color = (data[0].yearlyGrowthRate !== null && data[0].yearlyGrowthRate > 0) ? 'green' : (data[0].yearlyGrowthRate !== null && data[0].yearlyGrowthRate < 0) ? 'red' : '';

                targetAchievementRateSpan.textContent = (targetAchievementRate !== null ? targetAchievementRate.toFixed(2) + '%' : '');
                if (targetAchievementRate !== null) {
                    if (targetAchievementRate < 75.9) {
                        targetAchievementRateSpan.style.color = 'red';
                    } else if (targetAchievementRate >= 76 && targetAchievementRate < 84.9) {
                        targetAchievementRateSpan.style.color = 'orange';
                    } else if (targetAchievementRate >= 85 && targetAchievementRate < 99.9) {
                        targetAchievementRateSpan.style.color = '#30d130';
                    } else {
                        targetAchievementRateSpan.style.color = 'green';
                    }
                }
            })
            .catch(error => console.error('There has been a problem with your fetch operation:', error));
    };

    submitButton.addEventListener('click', function() {
        dateSpan.textContent = yearSelect.value + '년 ' + monthSelect.value + '월';
        tbody.innerHTML = '';
        salesMonthSpan.textContent = '';
        salesMonthPercentSpan.textContent = '';
        salesYearSpan.textContent = '';
        salesYearPercentSpan.textContent = '';
        salesProfitSpan.textContent = '';
        salesProfitPercentSpan.textContent = '';
        targetAchievementRateSpan.textContent = '';
        fetchSalesData();
    });

    // Fetch sales data on page load
    fetchSalesData();

    // Update chart on page load
    updateChart();
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