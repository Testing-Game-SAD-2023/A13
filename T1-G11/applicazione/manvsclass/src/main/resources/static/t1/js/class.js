var parametro = location.search.split('parametro=')[1];
var stringa_metodo;
if(typeof parametro == "undefined" )
{
  stringa_metodo='/home';
}
else
{
	stringa_metodo='/home/'+parametro;
   switch(parametro) {
  case '1':
    stringa_metodo='/orderbydate';
    break;
  case '2':
    stringa_metodo='/orderbyname';
    break;
  case '3':
    stringa_metodo='/Dfilterby/Beginner';
    break; 
	case '4':
    stringa_metodo='/Dfilterby/Intermediate';
    break;
	case '5':
    stringa_metodo='/Dfilterby/Advanced';
    break;
  default:
    break;
}
}
    fetch(stringa_metodo)
        .then(response => response.json())
        .then(data => {
            let classutList = document.getElementById('classut-list');
            let rows = '';
            data.forEach(home => {
					
                rows += `

<details>
		<summary>
			${home.name}
		</summary>
	<p>
		<span class="badge badge-info">${home.category[0]}</span><span class="badge badge-info">${home.category[1]}</span> <span class="badge badge-info">${home.category[2]}</span> &emsp;&emsp;&emsp;&emsp;
		<small class="date text-success">${home.difficulty}<span class="date text-muted">&emsp;&emsp;&emsp;&emsp;${home.date}</span></small>
	</p>
	<p>
		<small class="date text-muted">${home.description} </small>
	</p>
	<p>
		<button type="button" class="btn btn-light" onclick="window.location.href='modificaClasse?parametro=${home.name}'">
			<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-pencil">
			<path d="M12.146.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1 0 .708l-10 10a.5.5 0 0 1-.168.11l-5 2a.5.5 0 0 1-.65-.65l2-5a.5.5 0 0 1 .11-.168l10-10zM11.207 2.5 13.5 4.793 14.793 3.5 12.5 1.207 11.207 2.5zm1.586 3L10.5 3.207 4 9.707V10h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.293l6.5-6.5zm-9.761 5.175-.106.106-1.528 3.821 3.821-1.528.106-.106A.5.5 0 0 1 5 12.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.468-.325z"></path>
			</svg>
		</button>
		<button type="button" onclick="eliminaClasse('${home.name}')" class="btn btn-icon btn-danger">
			<i class="fa fa-trash"></i>
		</button>
		<button type="button" onclick="downloadFile('${home.name}')"  class="btn btn-success btn-sm">
			<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-arrow-down-circle" viewBox="0 0 16 16">
			<path fill-rule="evenodd" d="M1 8a7 7 0 1 0 14 0A7 7 0 0 0 1 8zm15 0A8 8 0 1 1 0 8a8 8 0 0 1 16 0zM8.5 4.5a.5.5 0 0 0-1 0v5.793L5.354 8.146a.5.5 0 1 0-.708.708l3 3a.5.5 0 0 0 .708 0l3-3a.5.5 0 0 0-.708-.708L8.5 10.293V4.5z"></path>
			</svg>
                Download
		</button>
	</p>
</details>	


&emsp;&emsp;&emsp;&emsp;
																					
                `;

            });
            classutList.innerHTML = rows;
        })
        .catch(error => console.error(error));

function getLikes(name) {
    const endpoint = '/getLikes/'+name; // URL dell'endpoint API
    
    return fetch(endpoint)
        .then(response => response.text())
        .then(data => {
        console.log(data);                          // log del risultato della chiamata API
        document.getElementById(name).innerHTML=data;
        //document.write("<h1>Questo è un messaggio sulla pagina HTML.</h1>");

        return data;                                // restituisce il risultato della chiamata API
        })
        .catch(error => console.error(error));      // gestione degli errori
    }
    
function downloadFile(name) {
fetch('/downloadFile/' + name)
    .then(response => response.blob())
    .then(blob => {
    var link = document.createElement('a');
    link.href = window.URL.createObjectURL(blob);
    link.download = name+'.java';
    link.click();
    })
    .catch(error => console.error(error));
}

function eliminaClasse(name) {
fetch(`/delete/${name}`, {
    method: 'POST'
})
.then(response => {
    if (!response.ok) {
    throw new Error('Errore durante l\'eliminazione della classe.');
    }
    // gestisci la risposta del server
    location.reload();
})
.catch(error => console.error(error));
}

function newLike(name) {
fetch(`/newlike/${name}`, {
    method: 'POST'
})
.then(response => {
    if (!response.ok) {
    throw new Error('Errore durante il like.');
    }
    // gestisci la risposta del server
    location.reload();
})
.catch(error => console.error(error));
}