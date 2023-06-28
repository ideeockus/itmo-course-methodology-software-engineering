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
    return
  }

  getPatientCard(userToken).then((resp_json) => {
      let patient = resp_json['patientCard']
      let p_name = patient['name'];
      let p_age = patient['age'];
      let p_phone = patient['phone'];
      let p_email = patient['email'];
      let p_address = patient['address'];
      let p_familiars = patient['familiars'];
      let p_state = patient['state'];

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

        let color_class = {
            'NotifiedSuccessful': "green_card",
            'NotProcessed': "blue_card",
            'FailedToNotify': "red_card",
            'ProcessedUnsuccessful': "yellow_card",
        }[familiar['state']];
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

        list_wrapper.appendChild(greenCardDiv);
    });
}

async function getPatientCard(token) {
    let response = await fetch("/get_patient_card", {
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
}

function edit_contact()
{
  this.document.getElementById("pacient_edit_form").setAttribute("style","display:none;");
  this.document.getElementById("contact_edit_form").setAttribute("style","display:block;");
  this.document.getElementById("contact_add_form").setAttribute("style","display:none;");

  let height = this.document.getElementById("contact_edit_form").offsetWidth + 100;
  this.document.getElementById("hiddable_form").setAttribute("style","display:block; height: "+height+"px");
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

  fetch("/edit_patient_card", {
    method: "post",
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      patientId: patientCardData['patientCard']['id'],
      patientCard: {
          id: patientCardData['patientCard']['id'],
          name: formData.get("name"),
          age: patientCardData['patientCard']['age'],
          email: formData.get("email"),
          phone: formData.get("phone"),
          address: formData.get("home"),
//          photo_url: formData.get("avatar"),
          token: patientCardData['patientCard']['token'],
          familiars: patientCardData['patientCard']['familiars'],
          state: patientCardData['patientCard']['state'],
      }
    })
  })
  .then( (response) => {
     response.json().then(resp_json => {
        console.log('Request succeeded:', resp_json);
        window.location.replace("user_profile_v2.html?token="+patientCardData['patientCard']['token']);
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

  fetch("/add_patient_familiar", {
    method: "post",
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      patientId: patientCardData['patientCard']['id'],
      familiar: {
          name: formData.get("name"),
          homePhone: formData.get("phone_home"),
          workPhone: formData.get("phone_work"),
          homeAddress: formData.get("home"),
          workAddress: formData.get("work"),
          state: "NotProcessed",
      }
    })
  })
  .then( (response) => {
     response.json().then(resp_json => {
        console.log('Request succeeded:', resp_json);
        window.location.replace("user_profile_v2.html?token="+patientCardData['patientCard']['token']);
     })
     .catch(error => {
        console.log('Request error:', error);
     })
  }
  );
});



function popup_patient_scan_progress() {  
  let myDialog = document.createElement("dialog");
  myDialog.style.width = "50%";
  document.body.appendChild(myDialog)
  let text = document.createTextNode("Начинаем сканирование...");
  myDialog.appendChild(text);
  myDialog.showModal();

  myDialog.addEventListener('click', (event) => {
    myDialog.remove();
  })

  // const statuses = ["Loading step 1", "Loading step 2", "Loading step 3", "Loading step 4"];
  // let currentIndex = 0;


  // Create the progress bar element
  var myProgress = document.createElement("div");
  myProgress.id = "myProgress";

  // Add the CSS styles to the progress bar element
  myProgress.style.width = "100%";
  myProgress.style.backgroundColor = "grey";

  // Create the progress bar element
  var myBar = document.createElement("div");

  // Add the CSS styles to the progress bar element
  myBar.style.width = "1%";
  myBar.style.height = "30px";
  myBar.style.backgroundColor = "green";

  // Append the progress bar element to the progress element
  myProgress.appendChild(myBar);

  // Add the progress element to the document body
  myDialog.appendChild(myProgress);

  let states = ["Поиск воспоминаний...", "Продолжайте думать, идет запись участка мозга", "Думайте о своем объекте...", "Сконцентрируйтесь, мозг сканируется"]



  var i = 0;
  function move() {
    if (i == 0) {
      i = 1;
      // var elem = document.getElementById("myBar");
      var width = 1;
      var id = setInterval(frame, 500);
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
        } else {
          width++;
          myBar.style.width = width + "%";
        }
      }
    }
  } 
  move()
}