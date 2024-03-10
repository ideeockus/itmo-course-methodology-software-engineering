document.addEventListener('DOMContentLoaded', function() {
    fetchStatistics();
});

function fetchStatistics() {
    fetch('/statistics/') // Замените на полный путь к вашему API, если это необходимо.
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(statistics => {
        const tableBody = document.querySelector('.statistics-table tbody');
        // Очищаем текущее содержимое tbody
        tableBody.innerHTML = '';
        // Заполняем таблицу статистикой
        statistics.forEach(stat => {
            const row = tableBody.insertRow();
            const keyCell = row.insertCell(0);
            const valueCell = row.insertCell(1);
            keyCell.textContent = stat.key;
            valueCell.textContent = stat.value;
        });
    })
    .catch(error => {
        console.error('Failed to fetch statistics:', error);
    });
}
