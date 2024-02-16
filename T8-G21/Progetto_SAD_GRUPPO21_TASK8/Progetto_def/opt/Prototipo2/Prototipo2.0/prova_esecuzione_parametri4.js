const http = require('http');
const fs = require('fs');
const { exec, fork } = require('child_process');
exec ('command', function (error, stdout, stderr) {});

const server = http.createServer((req, res) => {
    if (req.url.startsWith('/api/')) {
		//Rimuove '/api/' dalla stringa dell'URL e sostituisce '+' con spazi
        var paramString = req.url.slice(5).replace(/\+/g, ' '); 

		var parametri = paramString.split(' ');//Converte la stringa in un array di stringhe
		
		var params = parametri[0];//salva il primo path (contenente il percorso fino alla classe .java)
		var filePath = params.substring(0, params.lastIndexOf("/"));//salva il path fino al package
		var prova = params.substring(params.lastIndexOf("/") + 1 );//salva il nome della classe.java
		var filename = prova.substring(0, prova.lastIndexOf("."));//salva il nome della classe.java
		var directoryName = filePath.substring(filePath.lastIndexOf("/") + 1);//salva il nome del package
		
		//Elimina la prima stringa dall'array, così da poterla aggiornare con i parametri sopra descritti
		parametri.shift(); 
		//concateniamo le stringhe
		parametri= "/"+ filePath + " "+ directoryName + " " + filename + " " + parametri[0] + " " + parametri[1]; 
		console.log (parametri);
		//Utilizza la stringa dei parametri come argomento per il comando
		const command = `sh robot_misurazione_utente.sh ${parametri}`; 
        exec(command,
            function (error, stdout, stderr) {
                if (error !== null) {
                    console.log(error);
                } else {
                    console.log('stdout: ' + stdout); console.log('stderr: ' + stderr);
                }
            });
			
		const childProcess = fork('child.js');
		childProcess.on('message', message => {
		console.log(`Messaggio del processo figlio: ${message}`);
		res.end('L\'operazione e\' andata a buon fine!\nPercorso di salvataggio: /data/StudentLogin/GameId/TestReport\n');
    });	

  } else {
    res.writeHead(404, { 'Content-Type': 'text/plain' });
    res.end('Pagina non trovata');
  }
});


const port = 3000;
server.listen(port, () => {
    console.log(`Server listening on port ${port}`);
});