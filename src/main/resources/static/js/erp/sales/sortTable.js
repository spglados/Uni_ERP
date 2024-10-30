(function() {
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
})();