const http = require('http');
const fs = require('fs');
const { exec, fork } = require('child_process');

const server = http.createServer((req, res) => {
    if (req.url.startsWith('/api/')) {
        res.setHeader('Access-Control-Allow-Origin', '*');
        let body = '';
        req.on('data', chunk => {
            body += chunk.toString(); // convert Buffer to string
        });
        req.on('end', () => {
            const codiceJava = body; // Il codice Java inviato nel corpo della richiesta POST

            //Rimuove '/api/' dalla stringa dell'URL e sostituisce '+' con spazi
            var paramString = req.url.slice(5).replace(/\+/g, ' '); 
            var parametri = paramString.split(' ');//Converte la stringa in un array di stringhe
            var params = parametri[0];//salva il primo path (contenente il percorso fino alla classe .java)
            var filePath = params.substring(0, params.lastIndexOf("/"));//salva il path fino al package
            var prova = params.substring(params.lastIndexOf("/") + 1 );
            var filename = prova.substring(0, prova.lastIndexOf("."));//salva il nome della classe.java
            var directoryName = filePath.substring(filePath.lastIndexOf("/") + 1);//salva il nome del package
            
            //Elimina la prima stringa dall'array, così da poterla aggiornare con i parametri sopra descritti
            parametri.shift(); 
            parametri= "/"+ filePath + " "+ directoryName + " " + filename + " " + parametri[0] + " " + parametri[1]; 
            console.log (parametri);

            //aggiunto codice ricezione classe di test
            //percorso salvataggi classe di test
            const percorsoFile = '/app/Serv/src/test/java/Tests/Test'+ filename + '.java';

            fs.writeFile(percorsoFile, codiceJava, err => { //scrittura del test ricevuto
                if (err) {
                    console.error(err);
                    res.status(500).send('Si è verificato un errore durante il salvataggio del file');
                } else {
                    const command = `sh robot_misurazione_utente.sh ${parametri}`; 
                    exec(command,
                        function (error, stdout, stderr) {
                            if (error !== null) {
                                console.log(error);
                            } else {
                                console.log('stdout: ' + stdout); console.log('stderr: ' + stderr);
                                //aggiunto
                                const csvContent = fs.readFileSync('/app/statistics.csv', 'utf8');
                                console.log(csvContent);

                                // Imposta gli header
                                res.setHeader('Content-Type', 'text/csv');
                                res.setHeader(
                                    'Content-Disposition',
                                    `attachment; filename="${filename}.csv"`
                                );

                                // Invia il contenuto del file come corpo della risposta
                                res.end(csvContent);
                            }
                        });
                }
            });
            
            /*const childProcess = fork('child.js');
            childProcess.on('message', message => {
                console.log(`Messaggio del processo figlio: ${message}`);
            });	*/
        });
    } /*else {
        res.writeHead(404, { 'Content-Type': 'text/plain' });
        res.end('Pagina non trovata');
    }*/

    else if (req.url.startsWith('/tests/')) {
        res.setHeader('Access-Control-Allow-Origin', '*');

        const path = req.url.split('/').slice(2); // Rimuovi il primo elemento vuoto e "/tests/"

        if (path.length >= 2) {
            const numero = path[0];
            const nome = path[1];
            console.log(`Valore di "numero": ${numero}, Valore di "nome": ${nome}`);

            const testPath = '/VolumeT8/FolderTreeEvo/Tests/' + numero + '/' + nome +'.java';
            fs.readFile(testPath, (err, data) => {
                if (err) {
                    res.statusCode = 500;
                    res.end('Errore nel leggere il file Java');
                } else {
                    res.setHeader('Content-Disposition', 'attachment; filename=yourfile.java');
                    res.setHeader('Content-Type', 'text/plain');
                    res.end(data);
                }
            });
        } else {
            console.log('Percorso della richiesta non contiene numero e nome');
        }
    } 
    
    else{
        res.end('Richiesta http per test non andata a buon fine');
    }
});

const port = 3080;
server.listen(port, () => {
    console.log(`Server listening on port ${port}`);
});