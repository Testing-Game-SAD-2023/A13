//Import di moduli importanti:
// http, creazione server HTTP
// fs, operazione sul FileSystem
// exec e fork, esecuzione di processi esterni
// url, ... 

const http = require('http');
const fs = require('fs');
const { exec, fork } = require('child_process');

// exec, utilizzata per eseguire comandi shell; prende come input una stringa che rappresenta il comando da eseguire
// e restituisce un oggetto ChildProcess, che rappresenta il processo figlio avviato per eseguire il comando

// fork, utilizzato per avviare nuovi processi Node.js; prende come input il percorso del file JavaScript da eseguire
// nel nuovo processo e restituisce un oggetto ChildProcess rappresentativo del processo figlio avviato.

const { url } = require('inspector');

//Creazione di un server HTTP utilizzando il metodo 'createServer'
console.log("(prova_esecuzione_parametri4.js) Creazione server HTTP...");

const server = http.createServer((req, res) => {

    //Verifica che la richiesta sia presso un endpoint API controllando che l'URL inizi con /api/
    console.log("(prova_esecuzione_parametri4.js) Verifica che la richiesta sia presso un endpoint API");

    if (req.url.startsWith('/api/')) {

        console.log("(prova_esecuzione_parametri4.js) endpoint /api/");
        //Imposta l'header per consentire le richieste da tutte le origini '*'
        res.setHeader('Access-Control-Allow-Origin', '*');

        //Gestione evento ricezione dei dati della richiesta concatenando i chunk ricevuti in una stringa
        let body = '';
        req.on('data', chunk => {
            body += chunk.toString(); // convert Buffer to string
        });

        //Gestione evento di completamento della ricezione dei dati della richiesta
        req.on('end', () => {

            //Memorizza il codice Java ricevuto nel corpo della richiesta POST
            console.log("(prova_esecuzione_parametri4.js) Memorizzazione codice Java ricevuto nel corpo della richiesta POST");

            const codiceJava = body; // Il codice Java inviato nel corpo della richiesta POST
            //console.log("(prova_esecuzione_parametri4.js) codiceJava: "+ codiceJava+"\n");
            console.log("(prova_esecuzione_parametri4.js) request URL: "+req.url)+"\n";

            /* Remove '/api/' from the URL string and replace '+' with spaces,
            convert the string into an array of strings.
            
            Params are passed in URL as /api/param1+param2+param3, so the
            output is an array of strings ['param1', 'param2', 'param3'].

            First element is the path of the class to test: /VolumeT8/FolderTreeEvo/Calcolatrice/CalcolatriceSourceCode/Calcolatrice.java,
            second element is the path to the test report: /VolumeT8/FolderTreeEvo/Calcolatrice/StudentLogin/Game39/Round39/Turn1/TestReport,
            third element is the name of the package where the csv file will be saved: /app
            and the fourth element is the user id: e.g. 1.

            ['VolumeT8/FolderTreeEvo/Calcolatrice/CalcolatriceSourceCode/Calcolatrice.java',
            '/VolumeT8/FolderTreeEvo/Calcolatrice/StudentLogin/Game39/Round39/Turn1/TestReport',
            '/app',
            '1']*/
 
            console.log("(prova_esecuzione_parametri4.js) Estrazione parametri...\n");
            /* /api/VolumeT8/FolderTreeEvo/Calcolatrice/CalcolatriceSourceCode/Calcolatrice.java+
                /VolumeT8/FolderTreeEvo/Calcolatrice/StudentLogin/Player1/Game93/Round93/Turn1/TestReport+
                /app+
                1
            */
            var parametri = req.url.slice(5).replace(/\+/g, ' ').split(' ');
            console.log("(prova_esecuzione_parametri4.js) parametri: "+ parametri);

            /* Parameters after splicing the URL string, replacing and splitting it parameters=[arg0,arg1,arg2,arg3]:
                ['VolumeT8/FolderTreeEvo/Calcolatrice/CalcolatriceSourceCode/Calcolatrice.java,
                '/VolumeT8/FolderTreeEvo/Calcolatrice/StudentLogin/Player1/Game93/Round93/Turn1/TestReport',
                '/app',
                '1']
            */

            /* arg0=sourcecode path of the class to test=classPath=VolumeT8/FolderTreeEvo/Calcolatrice/CalcolatriceSourceCode/Calcolatrice.java 
            className=name of the class under test=Calcolatrice
            packagePath=path of the directory containing the class file=VolumeT8/FolderTreeEvo/Calcolatrice/CalcolatriceSourceCode
            packageName=name of the directory conatining the class file=CalcolatriceSourceCode
            arg1=path of directory where the test will be saved=/VolumeT8/FolderTreeEvo/Calcolatrice/StudentLogin/Player1/Game93/Round93/Turn1/TestReport
            testPath=path of the sourcecode of the test=/VolumeT8/FolderTreeEvo/Calcolatrice/StudentLogin/Player1/Game93/Round93/Turn1/TestReport/TestCalcolatrice.java
            arg3=user id of the player=1
            */
          
            // Percorso della classe da testare
            var classPath = parametri[0];
            console.log("classPath: "+ classPath);

            // Class name
            /* The substring() method extracts the characters from a string, between two specified indices: (a) the
            last occurrence of the forward slash ("/") and (b) the last occurrence of the dot (".")
            and returns the new sub string which is the class name
            */
            var className = classPath.substring(classPath.lastIndexOf("/") + 1, classPath.lastIndexOf("."));
            console.log("className: "+ className);

            // Percorso del package della classe
            var packagePath = classPath.substring(0, classPath.lastIndexOf("/"));
            console.log("packagePath: "+ packagePath);

            // Nome del package della classe
            var packageName = packagePath.substring(packagePath.lastIndexOf("/") + 1);
            console.log("packageName: "+ packageName);

            // UserId of player
            var userId = parametri[3];
            console.log("userId: "+ userId);

            // Package test path
            var packageTestPath = parametri[1];
            console.log("packageTestPath: "+ packageTestPath);

            // Sourcecode path of the test
            var testPath = packageTestPath + '/Test' + className + '.java';
            console.log("testPath: "+ testPath);

            console.log("(prova_esecuzione_parametri4.js) Elimina il primo elemento dall'array così da poterlo aggiornare con i parametri sopra descritti");
            parametri.shift();

            // TODO: update array 'parametri' with the new (5) values expected by the script: 'robot_misurazione_utente.sh'
            /* After the shifting the array 'parametri' is now:
            [   '/VolumeT8/FolderTreeEvo/Calcolatrice/StudentLogin/Player1/Game93/Round93/Turn1/TestReport', (arg0)
                '/app',                                                                                      (arg1)
                '1']                                                                                         (arg2)          
            */
            parametri = "/" + packagePath + " " + packageName + " " + className + " " + parametri[0] + " " + parametri[1];
            //parametri = "/" + packagePath + " " + packageName + " " + className + " " + testPath + " " + parametri[1];
            console.log("'parametri' updated: "+ parametri);

            /* TODO:The class we receive from the student must be saved in a specific folder for each student
            in order to avoid conflicts between classes with the same name; EvoSuite needs to be executed
            in a folder dedicated to the student.
            EvoSuite will create and will be executed under a folder named: 'evosuite-report' where it will be save the
            student's test report but if two students execute the same test, the second student will overwrite
            the first student's report.
            */

            /* Example of filePath= /VolumeT8/FolderTreeEvo/Calcolatrice/StudentLogin/Player1/Game93/Round93/Turn1/TestReport/TestCalcolatrice.java
            where '/VolumeT8/FolderTreeEvo/Calcolatrice/StudentLogin/Player1/Game93/Round93/Turn1/TestReport/TestCalcolatrice.java' is testPath 
            */

            const filePath = testPath;
            console.log("filePath: "+ filePath);

            // Into 'filePath' we will save (copy) the test received from the student: 'TestCalcolatrice.java'
            // Create the directory where the test will be saved
            console.log("(prova_esecuzione_parametri4.js) Creation of the directory where the test will be saved "+ packageTestPath);
            fs.mkdirSync(packageTestPath, { recursive: true });
            //Save the received test into a file
            console.log("(prova_esecuzione_parametri4.js) Writing the received test...");
            fs.writeFile(filePath, codiceJava, err => { 
                if (err) {
                    console.error(err);
                    res.status(500).send('(prova_esecuzione_parametri4.js) Si è verificato un errore durante il salvataggio del file');
                } else {

                    // TODO: robot_misurazione_utente.sh should be executed with the new parameters
                    
                    /* Execution of the 'robot_misurazione_utente.sh' script passing the parameters extracted
                    from the request URL
                    */

                    console.log("(prova_esecuzione_parametri4.js) Execution of the script: 'robot_misurazione_utente.sh'\n");
                    const command = `sh robot_misurazione_utente.sh ${parametri}`;
                    exec(command,
                        function (error, stdout, stderr) {
                            if (error !== null) {
                                console.log(error);
                            } else {
                                console.log('stdout: ' + stdout);
                                console.log('stderr: ' + stderr);       
                               
                                //Reading the content of a file named 'GameData.csv'= /VolumeT8/FolderTreeEvo/Calcolatrice/StudentLogin/Player1/Game93/Round93/Turn1/TestReport/GameData.csv
                                console.log("(prova_esecuzione_parametri4.js) Lettura del file 'GameData.csv'...\n");
                                const csvContent = fs.readFileSync(packageTestPath + '/GameData.csv', 'utf8');
                                console.log(csvContent);

                                // Headers configuration of the HTTP response
                                res.setHeader('Content-Type', 'text/csv');
                                res.setHeader(
                                    'Content-Disposition',
                                    `attachment; filename="${className}.csv"`
                                );
                                
                                //Send the content of the file as the body of the HTTP response
                                console.log("(prova_esecuzione_parametri4.js) Invio del contenuto del file come corpo della risposta HTTP");
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

    // TODO: Storico da rivedere path
    else if (req.url.startsWith('/tests/')) {

        console.log("(prova_esecuzione_parametri4.js) endpoint " + req.url);
        res.setHeader('Access-Control-Allow-Origin', '*');

        const path = req.url.split('/').slice(2); // Rimuovi il primo elemento vuoto e "/tests/"
        console.log("(prova_esecuzione_parametri4.js) req.url: "+ req.url+"\n");
        console.log("(prova_esecuzione_parametri4.js) path: "+ path+"\n");

        let testPath = '/' + path.join('/');
        console.log("(prova_esecuzione_parametri4.js) Costruzione percorso completo del file di test Java...\n");
        console.log("(prova_esecuzione_parametri4.js) testPath: "+ testPath);

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
        // Rimozione delle cartelle di allenamento

        console.log("(prova_esecuzione_parametri4.js), la richiesta inizia con /remove-allenamento\n");
        console.log("(prova_esecuzione_parametri4.js) Rimozione delle cartelle di allenamento...\n");
        console.log("(prova_esecuzione_parametri4.js) req.url: "+ req.url);

        //e.g. pathToDelete = '/VolumeT8/FolderTreeEvo/Calcolatrice/StudentLogin/Player1/Allenamento
        const pathToDelete = '/VolumeT8/FolderTreeEvo' +
                                '/' + req.url.split('/')[2] + 
                                '/StudentLogin' +
                                '/Player' + req.url.split('/')[3] +
                                '/Allenamento';
        try {
            // Verifica se la cartella esiste
            console.log("(prova_esecuzione_parametri4.js) Esiste la cartella 'Allenamento'?");
            if (fs.existsSync(pathToDelete)) {
                // Rimuovi la cartella
                console.log("(prova_esecuzione_parametri4.js) Si, procediamo con la rimoazione...")
                fs.rmdirSync(pathToDelete, { recursive: true });
                console.log('(prova_esecuzione_parametri4.js) Cartella rimossa con successo:', pathToDelete);
                res.writeHead(200, { 'Content-Type': 'text/plain' });
                res.end('Cartella rimossa con successo');
            } else {

                //La cartella non esiste...
                console.log('(prova_esecuzione_parametri4.js)No, la cartella non esiste:', pathToDelete);
                res.writeHead(200, { 'Content-Type': 'text/plain' });
                res.end('La cartella non esiste');
            }
        } catch (err) {
            console.error('(prova_esecuzione_parametri4.js) Errore durante la rimozione della cartella:', err);
            res.writeHead(500, { 'Content-Type': 'text/plain' });
            res.end('Errore durante la rimozione della cartella');
        }
    } else {
        res.end('Richiesta http per test non andata a buon fine');
    }
});

const port = 3080;
server.listen(port, () => {
    console.log(`(prova_esecuzione_parametri4.js) Server listening on port ${port}`);
});