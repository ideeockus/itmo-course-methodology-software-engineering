document.addEventListener('DOMContentLoaded', function() {
  // Your code to be executed after the DOM is ready
  console.log('DOM content loaded');
  resize_contact_list();
  resize_contact_list();

  // var tmp_card_body_id = document.getElementById('tmp_card_body_id');
  // tmp_card_body_id.style.display = "none";

  // var userToken = prompt("Введите ваш персональный токен");
  // console.log('Got user token', userToken);

  // if (userToken !== null) {
  //   // User entered a value
  //   // Proceed with the rest of the script using the userInput
  //   console.log("User token: " + userToken);
  //   tmp_card_body_id.style.display = "block";

  // } else {
  //   // User clicked "Cancel" or dismissed the prompt
  //   // Handle this case as needed
  //   console.log("Prompt canceled or dismissed.");
  // }
});

window.addEventListener('resize', resize_contact_list, true);

function resize_contact_list()
{
  let contacts_width = this.document.getElementById("card").offsetWidth *3;
  this.document.getElementById("list_wrapper").setAttribute("style","width:"+ contacts_width +"px");
}