const getCookie = (name) => {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) return parts.pop().split(';').shift();
  return null;
}

const parseJwt = (token) => {
  try {
    return JSON.parse(atob(token.split('.')[1]));
  } catch (e) {
    return null;
  }
};

//TODO: questo è solo per DEMO, i dati del profilo devono essere contenuti in un model
document.addEventListener("DOMContentLoaded", (e) => {
      var userId = document.getElementById("user_id").innerHTML;
      console.log(userId)

      var userAPIString = '/students_list/{0}'.replace('{0}', userId);

      // Get user infos
      $.ajax({
        url: userAPIString,
        type: 'GET',
        timeout: 30000,
        success: function (data, textStatus, xhr) {
          document.getElementById("usernameField").innerText = data.email + " - " + data.name + " " + data.surname;

          document.getElementById("studiesField").innerText = data.studies;
        },
        error: function (xhr, textStatus, errorThrown) {
          console.error("Errore durante il recupero dei dati utente:", errorThrown);
          swal("Errore","Errore durante il recupero dei dati utente","error");
        }
      });
});