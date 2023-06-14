document.addEventListener('DOMContentLoaded', function() {
  // Your code to be executed after the DOM is ready
  console.log('DOM content loaded');

  var tmp_card_body_id = document.getElementById('tmp_card_body_id');
  tmp_card_body_id.style.display = "none";

  var userToken = prompt("Введите ваш персональный токен");
  console.log('Got user token', userToken);

  if (userToken !== null) {
    // User entered a value
    // Proceed with the rest of the script using the userInput
    console.log("User token: " + userToken);
    tmp_card_body_id.style.display = "block";

  } else {
    // User clicked "Cancel" or dismissed the prompt
    // Handle this case as needed
    console.log("Prompt canceled or dismissed.");
  }
});