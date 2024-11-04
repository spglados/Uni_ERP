window.onload = function() {
  const currentDate = new Date();
  const formattedDate = formatDate(currentDate);
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