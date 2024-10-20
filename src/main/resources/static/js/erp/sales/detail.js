document.addEventListener('DOMContentLoaded', function() {
    const submitButton = document.getElementById('submit-btn');
    submitButton.addEventListener('click', function() {
        const yearSelect = document.getElementById('selectYear');
        const monthSelect = document.getElementById('selectMonth');
        const year = yearSelect.value;
        const month = monthSelect.value;

        $.ajax({
            type: 'GET',
            url: '/erp/sales/details',
            data: {
                year: year,
                month: month
            },
            success: function(data) {
                console.log(data);
            },
            error: function(error) {
                console.error(error);
            }
        });
    });
});