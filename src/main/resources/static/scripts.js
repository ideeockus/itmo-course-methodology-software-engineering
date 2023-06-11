// Получаем форму по её идентификатору
const create_request_form = document.getElementById('create_request_form');

// Обработчик события отправки формы
create_request_form.addEventListener('submit', (event) => {
  event.preventDefault(); // Отменяем стандартное поведение формы (перезагрузку страницы)

  const formData = new FormData(create_request_form);
  const jsonObject = {};

  // Преобразуем данные формы в JSON-объект
  for (const [key, value] of formData.entries()) {
    jsonObject[key] = value;
  }

  const jsonData = JSON.stringify(jsonObject);

  // Отправляем AJAX-запрос
  const xhr = new XMLHttpRequest();
  xhr.open('POST', '/create_request');
  xhr.setRequestHeader('Content-Type', 'application/json');
  xhr.onload = function () {
    if (xhr.status === 200) {
      // Обработка успешного ответа сервера
      console.log('Request succeeded:', xhr.responseText);
    } else {
      // Обработка ошибки сервера
      console.error('Request failed. Status:', xhr.status);
    }
  };
  xhr.send(jsonData);
});