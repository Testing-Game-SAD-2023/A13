var dominio=location.hostname;

document.addEventListener("DOMContentLoaded", function () {
  const achievementsPromise = fetch('/achievements/list').then(response => response.json());
  const statisticsPromise = fetch('/statistics/list').then(response => response.json());

  Promise.all([achievementsPromise, statisticsPromise])
      .then(([achievementsData, statisticsData]) => {

          const achievementsTable = $('#achievementsTable').DataTable({
              data: achievementsData,
              columns: [
                  { data: 'name' },
                  { data: 'description' },
                  {
                    "data": 'statistic',
                    "mRender": function (data) { return statisticsData.find((element) => element.id == data).name }
                  },
                  { data: 'progressRequired' },
                  {
                    "mData": null,
                    "bSortable": false,
                    "mRender": function () { return '<a href=#/>' + 'Delete' + '</a>'; }
                  }
              ]
          });

            const statisticsTable = $('#statisticsTable').DataTable({
                data: statisticsData,
                columns: [
                    { data: 'name' },
                    { data: 'gamemode' },
                    { data: 'role' },
                    { data: 'robot' },
                    {
                      "data": "id",
                      "bSortable": false,
                      "mRender": function (data, type, row, meta) { return '<button class="btn btn-danger" onclick="deleteStatistic(\'' + data + '\');">' + 'Delete' + '</button>'; }
                    }
                ]
            });

          $("#achievementsTable").css({"display":"block"});
          $("#statisticsTable").css({"display":"block"});

          document.getElementById("statistics-panel").style.display = 'none';
      })
      .catch(error => {
          console.error('Errore durante la richiesta:', error);
      });
});

function showStatistics() {
    document.getElementById("statistics-panel").style.display = 'block';
    document.getElementById("achievements-panel").style.display = 'none';
}

function showAchievements() {
    document.getElementById("statistics-panel").style.display = 'none';
    document.getElementById("achievements-panel").style.display = 'block';
}

function deleteStatistic(statID) {
    swal({
          title: "Warning",
          text: "Are you sure you want to delete this statistic?",
          icon: "warning",
          buttons: [
            'No',
            'Yes'
          ],
          dangerMode: true,
        }).then(function(isConfirm) {
          if (isConfirm) {
              fetch('/statistics/' + statID,  {
                    method: 'DELETE'
                }).then(() => {
                    window.location.replace("/achievements");
                });
          }
        })
}