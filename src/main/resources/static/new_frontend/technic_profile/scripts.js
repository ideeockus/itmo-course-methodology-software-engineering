function addEquipment(id, name, type, status, location, serviceDate, serviceLife) {
    var table = document.getElementById("equipmentTable");
    var newRow = table.insertRow(-1);

    // Добавляем скрытое поле с ID
    var hiddenIdCell = newRow.insertCell(0);
    hiddenIdCell.innerHTML = `<input type="hidden" name="equipmentId" value="${id}">`;
    hiddenIdCell.style.display = 'none';

    // Остальные ячейки
    var nameCell = newRow.insertCell(1);
    var typeCell = newRow.insertCell(2);
    var statusCell = newRow.insertCell(3);
    var locationCell = newRow.insertCell(4);
    var serviceDateCell = newRow.insertCell(5);
    var serviceLifeCell = newRow.insertCell(6);
    var actionCell = newRow.insertCell(7);

    // Заполнение ячеек данными
    nameCell.textContent = name;
    typeCell.textContent = type;
    statusCell.textContent = status;
    locationCell.textContent = location;
    serviceDateCell.textContent = serviceDate;
    serviceLifeCell.textContent = serviceLife;

    // Добавление кнопки "Изменить"
    actionCell.innerHTML = '<button onclick="openEditModal(this.parentNode.parentNode)">Изменить</button>';
    actionCell.className = 'action-cell';

    newRow.className = "equipment-row";
}


// Получение модального окна
var modal = document.getElementById("editModal");
// Получение элемента <span>, который закрывает модальное окно
var span = document.getElementsByClassName("close")[0];

// Когда пользователь кликает на <span> (x), закрываем модальное окно
span.onclick = function() {
    modal.style.display = "none";
}

// Когда пользователь кликает в любое место вне модального окна, закрываем его
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}


// function editEquipment(button) {
//     // Отображаем модальное окно
//     modal.style.display = "block";

//     var row = button.parentNode.parentNode;
//     var id = row.querySelector('input[name="equipmentId"]').value;

//     // Заполнение формы данными
//     document.getElementById("edit-id").value = id;
//     document.getElementById("edit-name").value = row.cells[1].innerText;
//     document.getElementById("edit-type").value = row.cells[2].innerText;
//     document.getElementById("edit-status").value = row.cells[3].innerText;
//     document.getElementById("edit-location").value = row.cells[4].innerText;
//     document.getElementById("edit-maintenanceDate").value = row.cells[5].innerText;
//     document.getElementById("edit-serviceLife").value = row.cells[6].innerText;
// }

// Функция обновления данных оборудования
function updateEquipment() {
    var id = document.getElementById("edit-id").value;
    var name = document.getElementById("edit-name").value;
    var type = document.getElementById("edit-type").value;
    var status = document.getElementById("edit-status").value;
    var location = document.getElementById("edit-location").value;
    var maintenanceDate = document.getElementById("edit-maintenanceDate").value;
    var serviceLife = document.getElementById("edit-serviceLife").value;

    // Тут код для отправки данных на сервер через fetch или XMLHttpRequest
    // Пример:
    fetch(`/equipment/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            id: id,
            name: name,
            type: type,
            status: status,
            location: location,
            maintenanceDate: maintenanceDate,
            serviceLife: serviceLife
        })
    })
    .then(response => response.json())
    .then(data => {
        console.log('Success:', data);
        loadEquipmentList();
        // Обновление данных в таблице или перезагрузка страницы
        modal.style.display = "none";
    })
    .catch((error) => {
        console.error('Error:', error);
    });
}



const statusDictionary = {
    "Available": "НА СКЛАДЕ",
    "Reserved": "ЗАРЕЗЕРВИРОВАНО",
    "Corrupted": "ПОЛОМКА",
    "NotAvailable": "НА ОБСЛУЖИВАНИИ",
    "Busy": "НА ВЫЕЗДЕ"
};

function getStatusKeyByValue(value) {
    return Object.keys(statusDictionary).find(key => statusDictionary[key] === value) || "";
}

function convertDateToInputFormat(date) {
    const parts = date.split(".");
    return `${parts[2]}-${parts[1]}-${parts[0]}`;
}


function clearTable() {
    const table = document.getElementById("equipmentTable");
    let rows = table.rows.length;
    while(rows > 1) {
        table.deleteRow(1);
        rows--;
    }
}

function getStatusByCode(statusCode) {
    return statusDictionary[statusCode] || statusCode;
}

document.addEventListener('DOMContentLoaded', loadEquipmentList);

function loadEquipmentList() {
    clearTable(); // очистка таблицы
    fetch("equipment/list")
    .then(response => response.json())
    .then(data => {
        data.forEach(equipment => {
            addEquipment(
                equipment.id, 
                equipment.name, 
                equipment.type, 
                getStatusByCode(equipment.status), 
                equipment.location || '', // Используем пустую строку, если значение отсутствует
                formatDate(equipment.maintenanceDate) || '', // Преобразование даты, если она присутствует
                equipment.serviceLife || '' // Используем пустую строку, если значение отсутствует
            );
        });
    })
    .catch(error => {
        console.error('Error fetching the equipment list:', error);
    });
};

// Функция для форматирования даты в более читаемый формат
function formatDate(dateString) {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleDateString('ru-RU'); // Измените локаль, если это необходимо
}

// Функция для отправки данных нового оборудования на сервер
function createEquipment() {
    var name = document.getElementById("edit-name").value;
    var type = document.getElementById("edit-type").value;
    var status = document.getElementById("edit-status").value;
    var location = document.getElementById("edit-location").value;
    var maintenanceDate = document.getElementById("edit-maintenanceDate").value;
    var serviceLife = document.getElementById("edit-serviceLife").value;

    fetch("/equipment/", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            name: name,
            type: type,
            status: status,
            location: location,
            maintenanceDate: maintenanceDate,
            serviceLife: serviceLife
        })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok ' + response.statusText);
        }
        return response.json();
    })
    .then(data => {
        console.log('Success:', data);
        loadEquipmentList();
        // Здесь можно добавить код для обновления таблицы с новым оборудованием
        modal.style.display = "none";
    })
    .catch((error) => {
        console.error('Error:', error);
    });
}




// Открывает модальное окно для редактирования существующего оборудования
function openEditModal(equipment_row) {
    // Заполнить форму данными
    modal.style.display = "block";

    var row = equipment_row;
    var id = row.querySelector('input[name="equipmentId"]').value;

    // Заполнение формы данными
    document.getElementById("edit-id").value = id;
    document.getElementById("edit-name").value = row.cells[1].innerText;
    document.getElementById("edit-type").value = row.cells[2].innerText;
    document.getElementById("edit-status").value = getStatusKeyByValue(row.cells[3].innerText);
    document.getElementById("edit-location").value = row.cells[4].innerText;
    document.getElementById("edit-maintenanceDate").value = convertDateToInputFormat(row.cells[5].innerText);
    document.getElementById("edit-serviceLife").value = row.cells[6].innerText;

    // Установить обработчик события для кнопки "Сохранить изменения"
    setModalSaveAction(() => updateEquipment(id));
}

// Открывает модальное окно для добавления нового оборудования
function openAddModal() {
    // Очищаем все поля формы
    document.getElementById("edit-id").value = '';
    document.getElementById("edit-name").value = '';
    document.getElementById("edit-type").value = '';
    document.getElementById("edit-status").value = 'Available'; // Задаем значение по умолчанию
    document.getElementById("edit-location").value = '';
    document.getElementById("edit-maintenanceDate").value = '';
    document.getElementById("edit-serviceLife").value = '';

    // Показываем модальное окно
    modal.style.display = "block";

    // Установить обработчик события для кнопки "Сохранить изменения"
    setModalSaveAction(createEquipment);
}

// Устанавливает функцию, которая будет вызываться при нажатии на кнопку "Сохранить изменения"
function setModalSaveAction(saveAction) {
    var saveButton = document.getElementById("saveButton");
    // Удаляем все предыдущие обработчики onclick, если они есть
    saveButton.onclick = null;
    // Устанавливаем новый обработчик onclick
    saveButton.onclick = saveAction;
}

// Присваиваем функцию кнопке для добавления нового оборудования
document.getElementById("add_new_equipment").addEventListener('click', openAddModal);
