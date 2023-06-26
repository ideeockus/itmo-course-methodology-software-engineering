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
        greenCardDiv.classList.add('green_card');

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
        greenCardDiv.appendChild(editBtnAnchor);

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