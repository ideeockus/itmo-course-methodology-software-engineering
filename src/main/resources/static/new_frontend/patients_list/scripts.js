document.addEventListener('DOMContentLoaded', function() {
  fetch('patients/get_all_patients')
    .then(response => response.json())
    .then(data => {
      const viewContainer = document.querySelector('.view-2');
      viewContainer.innerHTML = "";
      data.forEach(patient => {
        const patientDiv = document.createElement('div');
        patientDiv.className = 'view-3';
        
        const img = document.createElement('img');
        img.className = 'img';
        img.src = 'img/2x.png'; // Замените на соответствующий путь к изображению
        img.onclick = () => {
          window.location.href = `patient_card_view?token=${patient.userToken}`;
        };

        const nameDiv = document.createElement('div');
        nameDiv.className = 'text-wrapper-2';
        nameDiv.textContent = patient.name;

        const appointmentDiv = document.createElement('div');
        appointmentDiv.className = 'element';
        appointmentDiv.innerHTML = `${patient.appointmentDate}<br />Первое посещение`; // Адаптируйте текст в соответствии с вашими потребностями

        patientDiv.appendChild(img);
        patientDiv.appendChild(nameDiv);
        patientDiv.appendChild(appointmentDiv);

        viewContainer.appendChild(patientDiv);
      });
    })
    .catch(error => console.error('Error:', error));
});