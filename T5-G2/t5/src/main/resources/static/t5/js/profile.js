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

document.addEventListener("DOMContentLoaded", (e) => {
      var userId = parseJwt(getCookie("jwt")).userId;
      var apiString = '/students_list/{0}'.replace('{0}', userId);

      $.ajax({
        url: apiString,
        type: 'GET',
        timeout: 30000,
        success: function (data, textStatus, xhr) {
          //console.log(data);

          document.getElementById("usernameField").innerText = data.email + " - " + data.name + " " + data.surname;

          document.getElementById("studiesField").innerText = data.studies;
        },
        error: function (xhr, textStatus, errorThrown) {
          console.error("Errore durante il recupero delle Scalate:", errorThrown);
          swal("Errore","Errore durante il recupero delle Scalate","error");
        }
      });
});