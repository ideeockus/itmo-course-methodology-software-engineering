// Получаем форму по её идентификатору
const create_request_form = document.getElementById('create_request_form');

// Обработчик события отправки формы
create_request_form.addEventListener('submit', (event) => {
  event.preventDefault(); // Отменяем стандартное поведение формы (перезагрузку страницы)

  const formData = new FormData(create_request_form);
//  const jsonObject = {};
//
//  // Преобразуем данные формы в JSON-объект
//  for (const [key, value] of formData.entries()) {
//    jsonObject[key] = value;
//  }
//
//  const jsonData = JSON.stringify(jsonObject);

  fetch("/create_request", {
    method: "post",
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    //make sure to serialize your JSON body
    body: JSON.stringify({
      name: formData.get("name"),
      email: formData.get("email"),
      phone: formData.get("phone"),
      policyChecked: formData.get("policy_checked") == "on",
    })
  })
  .then( (response) => {
     //do something awesome that makes the world a better place
     response.json().then(resp_json => {
        console.log('Request succeeded:', resp_json);
        alert("Ваш персональный токен: " + response['userToken'])
        window.location.replace("user_profile.html");
     })
     .catch(error => {
        console.log('Request error:', error);
     })
  }
  );

//  // Отправляем AJAX-запрос
//  const xhr = new XMLHttpRequest();
//  xhr.open('POST', '/create_request');
//  xhr.setRequestHeader('Content-Type', 'application/json');
//  xhr.onload = function () {
//    if (xhr.status === 200) {
//      // Обработка успешного ответа сервера
//      console.log('Request succeeded:', xhr.responseText);
//      var response = JSON.parse(xhr.responseText);
//      console.log(response);
//      alert("Ваш персональный токен: " + response['userToken'])
//
////      window.location.replace("user_profile.html");
//    } else {
//      // Обработка ошибки сервера
//      console.error('Request failed. Status:', xhr.status);
//    }
//  };
//  xhr.send(jsonData);
});