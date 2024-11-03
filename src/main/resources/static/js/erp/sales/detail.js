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
    // Update the sales summary spans
    const salesMonthSpan = document.querySelector('.sales-month-span');
    const salesMonthPercentSpan = document.querySelector('.sales-month-percent-span');
    const salesYearSpan = document.querySelector('.sales-year-span');
    const salesYearPercentSpan = document.querySelector('.sales-year-percent-span');
    const salesProfitSpan = document.querySelector('.sales-profit-span');
    const salesProfitPercentSpan = document.querySelector('.sales-profit-percent-span');
    // Set default value to current year-month
    const currentDate = new Date();
    yearSelect.value = currentDate.getFullYear();
    monthSelect.value = currentDate.getMonth();
    if (monthSelect.value == 0) {
        yearSelect.value -= 1;
        monthSelect.value = 12;
    }
    dateSpan.textContent = yearSelect.value + '년 ' + monthSelect.value + '월';

    const fetchSalesData = () => {
        const year = yearSelect.value;
        const month = monthSelect.value;
        document.getElementById("loading-spinner").style.display = "block";
        fetch('/erp/sales/details?year=' + year + '&month=' + month)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                document.getElementById("loading-spinner").style.display = "none";
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
                // Calculate the total sales for the month and year
                const totalMonthlySales = data.reduce((acc, item) => acc + item.monthlySales, 0);
                const totalYearlySales = data.reduce((acc, item) => acc + item.yearlySales, 0);

                // Calculate the total profit for the year
                const totalProfit = data.reduce((acc, item) => acc + item.profit, 0);

                // Calculate the target achievement rate
                const totalLastYearSales = data.reduce((acc, item) => acc + item.lastYearSales, 0);
                const targetSales = totalLastYearSales * 1.05;
                const targetAchievementRate = (totalYearlySales / targetSales) * 100;
                const targetAchievementRateSpan = document.querySelector('.target-achievement-rate-span');

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
