// Function to update the data
function updateData(year, month) {
    $.ajax({
        type: 'GET',
        url: '/erp/sales/details',
        data: {
            year: year,
            month: month
        },
        success: function(data) {
            // Update the data in the HTML
            const thisMonthData = data.thisMonth;
            const lastMonthData = data.lastMonth;
            const thisYearData = data.thisYear;
            const lastYearData = data.lastYear;

            // Update the sales data
            const salesData = document.querySelector('.table-responsive table tbody');
            salesData.innerHTML = '';
            thisMonthData.forEach((item, index) => {
                const lastMonthItem = lastMonthData.find((i) => i.itemName === item.itemName) || {};
                const thisYearItem = thisYearData.find((i) => i.itemName === item.itemName) || {};
                const lastYearItem = lastYearData.find((i) => i.itemName === item.itemName) || {};

                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${item.itemName}</td>
                    <td>${item.totalQuantity}</td>
                    <td>${lastMonthItem.totalQuantity || ''}</td>
                    <td>${lastMonthItem.totalQuantity ? getPercentageDisplay((item.totalQuantity - lastMonthItem.totalQuantity) / lastMonthItem.totalQuantity * 100) : ''}</td>
                    <td>${thisYearItem.totalQuantity || ''}</td>
                    <td>${lastYearItem.totalQuantity || ''}</td>
                    <td>${lastYearItem.totalQuantity ? getPercentageDisplay((thisYearItem.totalQuantity || 0 - lastYearItem.totalQuantity) / lastYearItem.totalQuantity * 100) : ''}</td>
                    <td>${item.unitPrice * item.totalQuantity}</td>
                    <td></td>
                `;

                // Apply green color if positive, red if negative to percentage values
                const cells = row.querySelectorAll('td:nth-child(4), td:nth-child(7)');
                cells.forEach((cell) => {
                    const value = parseFloat(cell.textContent.replace('%', ''));
                    if (value > 0) {
                        cell.style.color = 'green';
                    } else if (value < 0) {
                        cell.style.color = 'red';
                    }
                });

                salesData.appendChild(row);
            });

            function getPercentageDisplay(percent) {
                if (percent > 0) {
                    return `+${percent.toFixed(2)}%`;
                } else if (percent < 0) {
                    return `${percent.toFixed(2)}%`;
                } else {
                    return '';
                }
            }

            // top section
            const topSection = document.querySelector('.d-flex.justify-content-between.mb-4');
            const dateSpan = topSection.querySelector('span[style="font-size: 24px; font-weight: bold; color: #007bff;"]');
            dateSpan.textContent = `${year}-${month.toString().padStart(2, '0')}`;

            const salesMonthSpan = topSection.querySelector('.sales-month-span');
            const salesMonthPercentSpan = topSection.querySelector('.sales-month-percent-span');
            const totalQuantity = thisMonthData.reduce((acc, item) => acc + item.totalQuantity, 0);
            const lastMonthTotalQuantity = lastMonthData.reduce((acc, item) => acc + item.totalQuantity, 0);
            const monthPercent = lastMonthTotalQuantity ? (((totalQuantity / lastMonthTotalQuantity) * 100) - 100).toFixed(2) : '';
            salesMonthSpan.textContent = totalQuantity.toLocaleString();

            // Update the percentage display
            if (monthPercent) {
                salesMonthPercentSpan.textContent = `(${monthPercent > 0 ? '+' : ''}${monthPercent}%)`;
                salesMonthPercentSpan.style.color = monthPercent < 0 ? 'red' : 'green';
            } else {
                salesMonthPercentSpan.textContent = '';
            }

            // Yearly sales data
            const salesYearSpan = topSection.querySelector('.sales-year-span');
            const salesYearPercentSpan = topSection.querySelector('.sales-year-percent-span');
            const thisYearTotalQuantity = thisYearData.reduce((acc, item) => acc + item.totalQuantity, 0);
            const lastYearTotalQuantity = lastYearData.reduce((acc, item) => acc + item.totalQuantity, 0);
            const yearPercent = lastYearTotalQuantity ? (((thisYearTotalQuantity / lastYearTotalQuantity) * 100) - 100).toFixed(2) : '';
            salesYearSpan.textContent = thisYearTotalQuantity.toLocaleString();

            // Update the yearly percentage display
            if (yearPercent) {
                salesYearPercentSpan.textContent = `(${yearPercent > 0 ? '+' : ''}${yearPercent}%)`;
                salesYearPercentSpan.style.color = yearPercent < 0 ? 'red' : 'green';
            } else {
                salesYearPercentSpan.textContent = '';
            }

            // Profit data
            const salesProfitSpan = topSection.querySelector('.sales-profit-span');
            const salesProfitPercentSpan = topSection.querySelector('.sales-profit-percent-span');
            const thisYearTotalProfit = thisYearData.reduce((acc, item) => acc + item.unitPrice * item.totalQuantity, 0);
            const lastYearTotalProfit = lastYearData.reduce((acc, item) => acc + item.unitPrice * item.totalQuantity, 0);
            const profitPercent = lastYearTotalProfit ? (((thisYearTotalProfit / lastYearTotalProfit) * 100) - 100).toFixed(2) : '';
            salesProfitSpan.textContent = thisYearTotalProfit.toLocaleString();

            // Update the profit percentage display
            if (profitPercent) {
                salesProfitPercentSpan.textContent = `(${profitPercent > 0 ? '+' : ''}${profitPercent}%)`;
                salesProfitPercentSpan.style.color = profitPercent < 0 ? 'red' : 'green';
            } else {
                salesProfitPercentSpan.textContent = '';
            }

            // Target achievement rate section
            const targetAchievementRateSpan = topSection.querySelector('.target-achievement-rate-span');
            const targetAchievementRate = lastYearTotalProfit ? (thisYearTotalProfit / lastYearTotalProfit) * 100 : 0;
            targetAchievementRateSpan.textContent = lastYearTotalProfit ? `${targetAchievementRate.toFixed(2)}%` : '';

        },
        error: function(error) {
            console.error(error);
        }
    });
}

// Function to get the percentage display
function getPercentageDisplay(percent) {
    if (percent > 0) {
        return `(+${percent.toFixed(2)}%)`;
    } else if (percent < 0) {
        return `(${percent.toFixed(2)}%)`;
    } else {
        return '';
    }
}

// Get the year and month select elements
const yearSelect = document.getElementById('selectYear');
const monthSelect = document.getElementById('selectMonth');

document.addEventListener('DOMContentLoaded', function() {
    const submitButton = document.getElementById('submit-btn');

    // Update the data when the page is loaded
    const currentYear = new Date().getFullYear();
    const currentMonth = new Date().getMonth() + 1;
    updateData(currentYear, currentMonth);

    // Update the data when the submit button is clicked
    submitButton.addEventListener('click', function() {
        const year = yearSelect.value;
        const month = monthSelect.value;
        updateData(year, month);
    });
});