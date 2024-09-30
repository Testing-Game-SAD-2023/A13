var dominio=location.hostname;

document.addEventListener("DOMContentLoaded", function () {
  const achievementsPromise = fetch('/achievements/list').then(response => response.json());

  Promise.all([achievementsPromise])
      .then(([achievementsData]) => {

          const table = $('#achievementsTable').DataTable({
              data: achievementsData,
              columns: [
                  { data: 'name' },
                  { data: 'description' },
                  { data: 'statistic' },
                  { data: 'progressRequired' }
              ]
          });

          $("#achievementsTable").css({"display":"block"});
          console.log(achievementsData)
      })
      .catch(error => {
          console.error('Errore durante la richiesta:', error);
      });
});