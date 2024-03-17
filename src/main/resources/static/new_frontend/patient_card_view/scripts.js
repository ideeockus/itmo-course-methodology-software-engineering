document.addEventListener('DOMContentLoaded', function() {
  // Your code to be executed after the DOM is ready
  console.log('DOM content loaded');
  resize_contact_list();
  resize_contact_list();

  fillCardData();
});

window.addEventListener('resize', resize_contact_list, true);
let patientCardData = null;

function resize_contact_list()
{
  let contacts_width = this.document.getElementById("card").offsetWidth *3;
  this.document.getElementById("list_wrapper").setAttribute("style","width:"+ contacts_width +"px");
}

function patient_state_to_rus(state) {
  if (state == "Stage1") {
    return "Первое посещение";
  } else if (state == "Stage2") {
    return "Второе посещение";
  } else if (state == "Stage3") {
    return "Карта загружена";
  } else if (state == "Stage4") {
    return "Воспоминания стерты";
  } else if (state == "Stage5") {
    return "Отклонен";
  } else {
    return "Неизвестно"
  }
}

function fillCardData() {
  let urlParams = new URLSearchParams(window.location.search);
  let userToken = urlParams.get('token');
  let isDoctor = urlParams.get('isDoctor');

  if (isDoctor == "true") {
    console.log('is doctor');
    let pacient_scan_btn = document.getElementById('pacient_scan_btn');
    pacient_scan_btn.style.display = "block";
  }

  if (userToken === null) {
    console.log("userToken: " + userToken);
    return
  }

  getPatientCard(userToken).then((resp_json) => {
      let patient = resp_json
      let p_name = patient['name'];
      let p_age = patient['age'];
      let p_phone = patient['phone'];
      let p_email = patient['email'];
      let p_address = patient['address'];
      let p_familiars = patient['familiars'];
      let p_state = patient['state'];
      let memoryScan = patient['memoryScan'];
      let visit_dt = patient['appointmentDate'];

      let pacient_name = document.getElementById('pacient_name')
      let patient_phone = document.getElementById('patient_phone')
      let patient_email = document.getElementById('patient_email')
      let pacient_home = document.getElementById('pacient_home')
      let pacient_work = document.getElementById('pacient_work')
      let brainmap = document.getElementById('brainmap')
      let patient_state = document.getElementById('patient_state')
      let visit_dt_elem = document.getElementById('next_visit')

      pacient_name.textContent = p_name
      patient_phone.textContent = p_phone
      patient_email.textContent = p_email
      pacient_home.textContent = p_address
      pacient_work.textContent = p_address
      patient_state.textContent = patient_state_to_rus(p_state)
      visit_dt_elem.textContent = visit_dt
      // brainmap.href = ""

      if (memoryScan === null) {
        console.log('has no memory scan');
        let patient_mem_erase_btn = document.getElementById('patient_mem_erase_btn');
        patient_mem_erase_btn.disabled=true;
        patient_mem_erase_btn.style.backgroundColor = "#aaaaaa";
        patient_mem_erase_btn.style.cursor = "not-allowed";
        patient_mem_erase_btn.onclick=null;
      } else {
        let brainmap = document.getElementById("brainmap");
        let scanDate = new Date(memoryScan['dateTime']).toLocaleString('ru-RU', {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
        brainmap.textContent = "Карта воспоминаний за " + scanDate
      }

      fillContacts(patient['familiars'])
  });
}

function fillContacts(familiars) {
    let list_wrapper = document.getElementById('list_wrapper')


    // Step 2: Select all elements with the specified classes within the parent element
    const elementsToRemove = list_wrapper.querySelectorAll('.red_card, .green_card, .yellow_card');

    // Step 3: Loop through the selected elements and remove each one
    elementsToRemove.forEach(element => {
      element.parentNode.removeChild(element);
    });

    familiars.forEach((familiar) => {
        // Create the green_card div
        const greenCardDiv = document.createElement('div');

        let color_classes = {
            'NotifiedSuccessful': "green_card",
            'NotProcessed': "blue_card",
            'FailedToNotify': "red_card",
            'ProcessedUnsuccessful': "yellow_card",
        };
        let color_class = color_classes[familiar['state']];
        greenCardDiv.classList.add(color_class);

        // Create the h3 element for the pacient_name
        const h3Element = document.createElement('h3');
        h3Element.classList.add('pacient_name');
        h3Element.textContent = familiar['name'];

        // Create the first p element for the phone number
        const firstPElement = document.createElement('p');
        firstPElement.textContent = familiar['homePhone'];

        // Create the i element for "место жительства"
        const firstIElement = document.createElement('i');
        firstIElement.textContent = familiar['workPhone'];

        // Create the second p element for the phone number
        const secondPElement = document.createElement('p');
        secondPElement.textContent = familiar['homeAddress'];

        // Create the second i element for "место работы"
        const secondIElement = document.createElement('i');
        secondIElement.href = '';
        secondIElement.textContent = familiar['workAddress'];

        // Create the edit_btn anchor element
//        const editBtnAnchor = document.createElement('a');
//        editBtnAnchor.href = '';
//        editBtnAnchor.classList.add('edit_btn');
//        editBtnAnchor.textContent = 'Редактировать';

        // Append all the elements to their respective parents
        greenCardDiv.appendChild(h3Element);
        greenCardDiv.appendChild(firstPElement);
        greenCardDiv.appendChild(firstIElement);
        greenCardDiv.appendChild(secondPElement);
        greenCardDiv.appendChild(secondIElement);
//        greenCardDiv.appendChild(editBtnAnchor);

        // Сохранение ID знакомого в атрибуте data-id
        greenCardDiv.setAttribute('data-id', familiar['id']);

        // Добавление обработчика события onclick, который вызывает setNextFamiliarStatus с текущим ID
        greenCardDiv.onclick = function() {
          const familiarId = this.getAttribute('data-id');
          setNextFamiliarStatus(familiarId)
              .then(nextState => {
                  let newColorClass = color_classes[nextState];
                  // Удаляем все текущие классы состояний
                  this.classList.remove("green_card", "blue_card", "red_card", "yellow_card");
                  // Добавляем новый класс, соответствующий обновленному статусу
                  this.classList.add(newColorClass);
              })
              .catch(error => {
                  console.error('Error updating familiar status:', error);
              });
      };


        list_wrapper.appendChild(greenCardDiv);
    });
}

async function getPatientCard(token) {
    let response = await fetch("/patients/by_token", {
        method: "post",
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json',
        },
        //make sure to serialize your JSON body
        body: JSON.stringify({
          patientToken: token,
        })
   })

//   (response) => {
    let resp_json = await response.json();
    console.log('Request succeeded:', resp_json);
    patientCardData = resp_json;

    return resp_json

//    .catch(error => {
//        console.log('Request error:', error);
//    })
//   }
}

function update_ui_patient_data() {
  let pacient_name = document.getElementById('pacient_name')
  let patient_phone = document.getElementById('patient_phone')
  let patient_email = document.getElementById('patient_email')
  let pacient_home = document.getElementById('pacient_home')
  let pacient_work = document.getElementById('pacient_work')
  let brainmap = document.getElementById('brainmap')

  pacient_name.textContent = p_name
  patient_phone.textContent = p_phone
  patient_email.textContent = p_email
  pacient_home.textContent = p_address
  pacient_work.textContent = p_address
  brainmap.href = ""
}

function hiddable_close()
{
  this.document.getElementById("hiddable_form").setAttribute("style","display:none;");
}

function edit_card()
{
  // this.document.body.setAttribute("style","overflow:hidden;");
  this.document.getElementById("pacient_edit_form").setAttribute("style","display:block;");
  this.document.getElementById("contact_edit_form").setAttribute("style","display:none;");
  this.document.getElementById("contact_add_form").setAttribute("style","display:none;");
  
  let height = this.document.getElementById("pacient_edit_form").offsetWidth + 100;
  this.document.getElementById("hiddable_form").setAttribute("style","display:block; height: "+height+"px");

  // form filling
  let form = document.getElementById("pacient_edit_form");
  // Заполняем поля формы данными
  form.querySelector('input[name="name"]').value = patientCardData['name'] || '';
  form.querySelector('input[name="email"]').value = patientCardData['email'] || '';
  form.querySelector('input[name="phone"]').value = patientCardData['phone'] || ''; // Предполагая, что phone - это объект с полем number
  form.querySelector('input[name="home"]').value = patientCardData['address'] || ''; // Если адрес предполагается разделить, здесь может потребоваться дополнительная логика  
  let stateValue = patientCardData['state'];
  form.querySelector('select[name="state"]').value = stateValue;
  
  // Устанавливаем дату и время визита
  if (patientCardData['appointmentDate']) {
      form.querySelector('input[name="visit"]').value = patientCardData['appointmentDate'];
  }
}

function edit_contact()
{
  this.document.getElementById("pacient_edit_form").setAttribute("style","display:none;");
  this.document.getElementById("contact_edit_form").setAttribute("style","display:block;");
  this.document.getElementById("contact_add_form").setAttribute("style","display:none;");

  let height = this.document.getElementById("contact_edit_form").offsetWidth + 100;
  this.document.getElementById("hiddable_form").setAttribute("style","display:block; height: "+height+"px");

  // Собрать данные
  const formData = new FormData(form);
  const p_name = formData.get('name');
  const p_phone = formData.get('phone_home');
  const p_home = formData.get('home');
  const p_phone_work = formData.get('phone_work');
  const work = formData.get('work');
  const avatar = formData.get('avatar');

  let pacient_name = document.getElementById('pacient_name')
  let patient_phone = document.getElementById('patient_phone')
  let patient_email = document.getElementById('patient_email')
  let pacient_home = document.getElementById('pacient_home')
  let pacient_work = document.getElementById('pacient_work')
  let brainmap = document.getElementById('brainmap')

  pacient_name.textContent = p_name
  patient_phone.textContent = p_phone
  patient_email.textContent = p_email
  pacient_home.textContent = p_phone
  pacient_work.textContent = p_phone_work
  brainmap.href = ""
}

function add_contact()
{
  this.document.getElementById("pacient_edit_form").setAttribute("style","display:none;");
  this.document.getElementById("contact_edit_form").setAttribute("style","display:none;");
  this.document.getElementById("contact_add_form").setAttribute("style","display:block;");

  let height = this.document.getElementById("contact_add_form").offsetWidth + 100;
  this.document.getElementById("hiddable_form").setAttribute("style","display:block; height: "+height+"px");
}




// обработка редактирования карточки
const pacient_edit_form = document.getElementById('pacient_edit_form');
pacient_edit_form.addEventListener('submit', (event) => {
  event.preventDefault(); // Отменяем стандартное поведение формы (перезагрузку страницы)

  let formData = new FormData(pacient_edit_form);
//  let date = document.getElementById('dateOfTimeslots').value

  fetch("patients/edit_patient_card", {
    method: "post",
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      id: patientCardData['id'],
      name: formData.get("name"),
      email: formData.get("email"),
      phone: formData.get("phone"),
      age: patientCardData['age'],
      address: formData.get("home"),
      // state: patientCardData['state'],
      state: formData.get("state"),
    })
  })
  .then( (response) => {
     response.json().then(resp_json => {
        console.log('Request succeeded:', resp_json);
        window.location.replace("patient_card_view?token="+patientCardData['userToken']);
     })
     .catch(error => {
        console.log('Request error:', error);
     })
  }
  );
});


// обработка добавления знакомого
const contact_add_form = document.getElementById('contact_add_form');
contact_add_form.addEventListener('submit', (event) => {
  event.preventDefault(); // Отменяем стандартное поведение формы (перезагрузку страницы)

  let formData = new FormData(contact_add_form);
//  let date = document.getElementById('dateOfTimeslots').value

  fetch("patients/add_patient_familiar", {
    method: "post",
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      patientId: patientCardData['id'],
      name: formData.get("name"),
      homePhone: formData.get("phone_home"),
      workPhone: formData.get("phone_work"),
      homeAddress: formData.get("home"),
      workAddress: formData.get("work"),
      state: "NotProcessed",
      // familiar: {
      //     name: formData.get("name"),
      //     homePhone: formData.get("phone_home"),
      //     workPhone: formData.get("phone_work"),
      //     homeAddress: formData.get("home"),
      //     workAddress: formData.get("work"),
      //     state: "NotProcessed",
      // }
    })
  })
  .then( (response) => {
     response.json().then(resp_json => {
        console.log('Request succeeded:', resp_json);
        window.location.replace("patient_card_view?token="+patientCardData['userToken']);
     })
     .catch(error => {
        console.log('Request error:', error);
     })
  }
  );
});


function popup_patient_scan_progress() {
    // Создание и стилизация диалога
    let myDialog = document.createElement("dialog");
    myDialog.style.width = "50%";
    document.body.appendChild(myDialog);

    // Сообщение о начале сканирования
    let textNode = document.createTextNode("Начинаем сканирование...");
    myDialog.appendChild(textNode);

    // Создание и стилизация контейнера прогресс-бара
    let myProgress = createProgressBarContainer();
    myDialog.appendChild(myProgress.barContainer);

    // Добавление текста состояния сканирования
    let states = [
        "Поиск воспоминаний...",
        "Продолжайте думать, идет запись участка мозга",
        "Думайте о своем объекте...",
        "Сконцентрируйтесь, мозг сканируется"
    ];

    // Добавление кнопки "Завершить сканирование"
    let finishButton = document.createElement("button");
    finishButton.style.margin = "20px";
    finishButton.disabled = true;
    finishButton.textContent = "Завершить сканирование";
    finishButton.addEventListener('click', () => {
      recordMemoryScan(patientCardData['id'], "Воспоминания", "some data");
      scanFinished();
      myDialog.close();
    });
    myDialog.appendChild(finishButton);

    // Показ диалога
    myDialog.showModal();

    // Запуск анимации прогресс-бара
    animateProgressBar(myProgress.bar, textNode, states, finishButton);
}

function createProgressBarContainer() {
    let myProgress = document.createElement("div");
    myProgress.style.width = "100%";
    myProgress.style.backgroundColor = "grey";

    let myBar = document.createElement("div");
    myBar.style.width = "1%";
    myBar.style.height = "30px";
    myBar.style.backgroundColor = "green";

    myProgress.appendChild(myBar);

    return { barContainer: myProgress, bar: myBar };
}

function animateProgressBar(bar, textNode, states, finishButton) {
    let i = 0;
    let width = 1;
    let id = setInterval(frame, 500);

    function frame() {
        if (Math.random() < 0.18) {
            textNode.textContent = states[Math.floor(Math.random() * states.length)];
        }

        if (width >= 100) {
            clearInterval(id);
            textNode.textContent = "Сканирование завершено!";
            finishButton.disabled = false;
        } else {
            width++;
            bar.style.width = width + "%";
        }
    }
}



// function popup_patient_scan_progress() {  
//   let myDialog = document.createElement("dialog");
//   myDialog.style.width = "50%";
//   document.body.appendChild(myDialog)
//   let text = document.createTextNode("Начинаем сканирование...");
//   myDialog.appendChild(text);
//   myDialog.showModal();

//   myDialog.addEventListener('click', (event) => {
//     myDialog.remove();
//   })

//   // const statuses = ["Loading step 1", "Loading step 2", "Loading step 3", "Loading step 4"];
//   // let currentIndex = 0;


//   // Create the progress bar element
//   var myProgress = document.createElement("div");
//   myProgress.id = "myProgress";

//   // Add the CSS styles to the progress bar element
//   myProgress.style.width = "100%";
//   myProgress.style.backgroundColor = "grey";

//   // Create the progress bar element
//   var myBar = document.createElement("div");

//   // Add the CSS styles to the progress bar element
//   myBar.style.width = "1%";
//   myBar.style.height = "30px";
//   myBar.style.backgroundColor = "green";

//   // Append the progress bar element to the progress element
//   myProgress.appendChild(myBar);

//   // Add the progress element to the document body
//   myDialog.appendChild(myProgress);

//   let states = ["Поиск воспоминаний...", "Продолжайте думать, идет запись участка мозга", "Думайте о своем объекте...", "Сконцентрируйтесь, мозг сканируется"]



//   var i = 0;
//   function move() {
//     if (i == 0) {
//       i = 1;
//       // var elem = document.getElementById("myBar");
//       var width = 1;
//       var id = setInterval(frame, 500);
//       function frame() {
//         let r = Math.floor(Math.random()*100);
//         if (r < 18) {
//           var state = states[Math.floor(Math.random()*states.length)];
//           text.textContent = state;
//         }
        

//         if (width >= 100) {
//           clearInterval(id);
//           i = 0;
//           text.textContent = "Сканирование завершено!";
//           scanFinished();
//         } else {
//           width++;
//           myBar.style.width = width + "%";
//         }
//       }
//     }
//   } 
//   move()
// }

function scanFinished() {
  let patient_mem_erase_btn = document.getElementById('patient_mem_erase_btn');
  patient_mem_erase_btn.disabled=false;
  patient_mem_erase_btn.style.backgroundColor = "#26a9e0";
  patient_mem_erase_btn.style.cursor = "pointer";
  patient_mem_erase_btn.onclick=popup_patient_mem_erase_progress;
}


function popup_patient_mem_erase_progress() {  
  let myDialog = document.createElement("dialog");
  myDialog.style.width = "50%";
  document.body.appendChild(myDialog)
  let text = document.createTextNode("Начинаем стирание...");
  myDialog.appendChild(text);
  myDialog.showModal();

  myDialog.addEventListener('click', (event) => {
    myDialog.remove();
  })

  // Create the progress bar element
  var myProgress = document.createElement("div");
  myProgress.id = "myProgress";

  // Add the CSS styles to the progress bar element
  myProgress.style.width = "100%";
  myProgress.style.backgroundColor = "grey";
  myProgress.style.borderRadius = "10px";

  // Create the progress bar element
  var myBar = document.createElement("div");

  // Add the CSS styles to the progress bar element
  myBar.style.width = "1%";
  myBar.style.height = "50px";
  myBar.style.borderRadius = "10px";
  myBar.style.backgroundColor = "#26a9e0";

  // Append the progress bar element to the progress element
  myProgress.appendChild(myBar);

  // Add the progress element to the document body
  myDialog.appendChild(myProgress);

  let states = [
    "Поиск воспоминаний...",
    "Выполняется стирание воспоминаний...",
    "dd if=/dev/zero of=/patient/brain",
    "Следите за состоянием пациента...",
    "Стираем память...",
    "Очистка кэша памяти пациента...",
    "Процесс идет успешно... Аномалий не обнаружено..",
    "Прощай, прошлое! Здравствуй, новая страница жизни...",
    ]



  var i = 0;
  function move() {
    if (i == 0) {
      i = 1;
      // var elem = document.getElementById("myBar");
      var width = 1;
      var id = setInterval(frame, 1000);
      function frame() {
        let r = Math.floor(Math.random()*100);
        if (r < 18) {
          var state = states[Math.floor(Math.random()*states.length)];
          text.textContent = state;
        }

        if (width >= 100) {
          clearInterval(id);
          i = 0;
          text.textContent = "Сканирование завершено!";
          scanFinished();
        } else {
          width++;
          myBar.style.width = width + "%";
        }
      }
    }
  } 
  move()
}


// Функция для отправки данных сканирования воспоминаний на сервер
function recordMemoryScan(patientId, memoryItem, activityData) {
    // Здесь мы создаем объект данных, который соответствует ожидаемому формату сервера
    const memoryScanData = {
        patientId: patientId,
        memoryItem: memoryItem,
        activityData: activityData
    };

    // Выполняем POST запрос с помощью fetch API
    fetch('/memoryScans/record', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json', // Указываем тип содержимого
            // 'Authorization': 'Bearer ваш_токен' // Если нужна авторизация
        },
        body: JSON.stringify(memoryScanData) // Преобразуем данные в JSON
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.text(); // Или response.json() если ожидается JSON ответ
    })
    .then(data => {
        console.log('Memory scan recorded successfully:', data);
        // Действия после успешной записи
    })
    .catch(error => {
        console.error('Error during memory scan recording:', error);
    });
}


function setNextFamiliarStatus(familiarId) {
    return fetch(`patients/set_next_familiar_status/${familiarId}`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP status ${response.status}`);
        }
        return response.json();
    })
    .then(nextState => {
        console.log('Next state:', nextState);
        // Здесь можно обновить интерфейс пользователя с новым состоянием
        return nextState;
    })
    .catch(error => {
        console.error('Error setting the next familiar status:', error);
        // Здесь можно обработать ошибки
    });
}
