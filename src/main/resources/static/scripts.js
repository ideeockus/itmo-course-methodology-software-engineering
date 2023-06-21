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
  let checked_time_slots = document.getElementsByClassName('checked');
  if (checked_time_slots.length < 1) {
    alert("Вы не выбрали время");
    return
  }

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
//      time: checked_time_slots[0].innerHTML,
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
});
//
//<<<<<<< HEAD
////  // Отправляем AJAX-запрос
////  const xhr = new XMLHttpRequest();
////  xhr.open('POST', '/create_request');
////  xhr.setRequestHeader('Content-Type', 'application/json');
////  xhr.onload = function () {
////    if (xhr.status === 200) {
////      // Обработка успешного ответа сервера
////      console.log('Request succeeded:', xhr.responseText);
////      var response = JSON.parse(xhr.responseText);
////      console.log(response);
////      alert("Ваш персональный токен: " + response['userToken'])
////
//////      window.location.replace("user_profile.html");
////    } else {
////      // Обработка ошибки сервера
////      console.error('Request failed. Status:', xhr.status);
////    }
////  };
////  xhr.send(jsonData);
//});
//=======
//  const jsonData = JSON.stringify(jsonObject);
//
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
//});

function refresh_timeslots()
{
  // TODO: брать состояния слотов из базы и обновлять классы на free и full
  let timeslotList = document.getElementsByTagName('th');
  for (slot of timeslotList) {
    if (slot.classList.contains('checked'))
    {
      slot.classList.add('free');
      slot.classList.remove('checked');
    }
  }
}

function init()
{
  refresh_timeslots();
  let timeslotList = document.getElementsByTagName('th');
  for (slot of timeslotList) {
    slot.onclick = markTimeslotAsSelected;
  }
  
  let dateofTimeslots = document.getElementById('dateOfTimeslots');
  const cur_date = new Date();
  const end_date = new Date(cur_date);
  end_date.setDate(cur_date.getDate() + 14);
  dateofTimeslots.valueAsDate = cur_date;
  dateofTimeslots.setAttribute("min", cur_date.getFullYear().toString() + '-' + (cur_date.getMonth()+1).toString().padStart(2, '0') + '-' + cur_date.getDate().toString().padStart(2, '0'));
  dateofTimeslots.setAttribute("max", end_date.getFullYear().toString() + '-' + (end_date.getMonth()+1).toString().padStart(2, '0') + '-' + end_date.getDate().toString().padStart(2, '0'));
}
init();

function markTimeslotAsSelected()
{
  refresh_timeslots();
  if(this.classList.contains('free'))
  {
    this.classList.add('checked');
    this.classList.remove('free');
  }
  else if(this.classList.contains('full'))
  {
    alert("На это время кто-то уже успел занять всех врачей! Попробуйте выбрать другой слот.");
  }
}
