const http = require('http');
const fs = require('fs');
const { exec, fork } = require('child_process');
const { url } = require('inspector');

const server = http.createServer((req, res) => {
    if (req.url.startsWith('/api/')) {
        res.setHeader('Access-Control-Allow-Origin', '*');
        let body = '';
        req.on('data', chunk => {
            body += chunk.toString(); // convert Buffer to string
        });
        req.on('end', () => {
            const codiceJava = body; // Il codice Java inviato nel corpo della richiesta POST

            console.log(url);

            // Rimuove '/api/' dalla stringa dell'URL e sostituisce '+' con spazi. 
            // Converte la stringa in un array di stringhe
            var parametri = req.url.slice(5).replace(/\+/g, ' ').split(' ');

            // Percorso della classe da testare
            var classPath = parametri[0];

            // Nome della classe da testare
            var className = classPath.substring(classPath.lastIndexOf("/") + 1, classPath.lastIndexOf("."));

            // Percorso del package della classe
            var packagePath = classPath.substring(0, classPath.lastIndexOf("/"));

            // Nome del package della classe
            var packageName = packagePath.substring(packagePath.lastIndexOf("/") + 1);

            //Elimina la prima stringa dall'array, così da poterla aggiornare con i parametri sopra descritti
            parametri.shift();
            parametri = "/" + packagePath + " " + packageName + " " + className + " " + parametri[0] + " " + parametri[1];

            console.log(parametri);

            const filePath = '/app/Serv/src/test/java/Tests/Test' + className + '.java';

            fs.writeFile(filePath, codiceJava, err => { //scrittura del test ricevuto
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
                                    `attachment; filename="${className}.csv"`
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
        let testPath;
        console.log(path);

        if (path.length <= 2) {
            const test = path[0];
            const classe = path[1];
            testPath = '/VolumeT8/FolderTreeEvo/Allenamento/' + test + '/TestReport/Test' + classe + '.java';

        } else {
            const game = path[0];
            const round = path[1];
            const turn = path[2];
            const classe = path[3];

            console.log(`Valore di "gameId": ${game.replace('Game', '')}, Valore di "roundId": ${round.replace('Round', '')}, Valore di "turnId": ${turn.replace('Turn', '')}, Valore di "nomeClasse": ${classe}`);

            testPath = '/VolumeT8/FolderTreeEvo/' + classe + '/StudentLogin/' + game + '/' + round + '/' + turn + '/TestReport/Test' + classe + '.java';
        }

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
    } else if (req.url.startsWith('/remove-allenamento')) {
        // Rimozione delle cartelle
        const pathToDelete = '/VolumeT8/FolderTreeEvo/Allenamento';
        try {
            // Verifica se la cartella esiste
            if (fs.existsSync(pathToDelete)) {
                // Rimuovi la cartella
                fs.rmdirSync(pathToDelete, { recursive: true });
                console.log('Cartella rimossa con successo:', pathToDelete);
                res.writeHead(200, { 'Content-Type': 'text/plain' });
                res.end('Cartella rimossa con successo');
            } else {
                console.log('La cartella non esiste:', pathToDelete);
                res.writeHead(200, { 'Content-Type': 'text/plain' });
                res.end('La cartella non esiste');
            }
        } catch (err) {
            console.error('Errore durante la rimozione della cartella:', err);
            res.writeHead(500, { 'Content-Type': 'text/plain' });
            res.end('Errore durante la rimozione della cartella');
        }
    } else {
        res.end('Richiesta http per test non andata a buon fine');
    }
});

const port = 3080;
server.listen(port, () => {
    console.log(`Server listening on port ${port}`);
});