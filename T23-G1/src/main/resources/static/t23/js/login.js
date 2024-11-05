//statusChangeCallback() viene chiamata con i risultati restituiti da FB.getLoginStatus().
function statusChangeCallback(response) {
  console.log('statusChangeCallback');
  console.log(response);
  
  //Siamo loggati?
  if(response.status==='connected') {

    const access_token = response.authResponse.accessToken;

    //Richiesta all'API per recuperare email e nome
    FB.api('/me', {fields: 'name, email'}, function(response) {
                    //document.getElementById("profile").innerHTML = "Good to see you, " + response.name + ". i see your email address is " + response.email
                    console.log(JSON.stringify(response));

                    //Invio dati back-end (POST): accessToken + email + nome
                    //Dati da inviare nella richiesta POST (da convertire in formato JSON)
                    const data = {
                      email: response.email,
                      nome: response.name,
                      access_token: access_token
                    };

                    // Costruzione URL che includa i parametri di interesse (nome, email ed access_token)
                    const url = `https://blindly-harmless-oarfish.ngrok-free.app/login_with_facebook?nome=${data.nome}&email=${data.email}&access_token=${data.access_token}`

                    //Configuraazione della richiesta
                    const options = {
                      method: 'POST',
                      headers: {
                        'Content-Type': 'application/json'            // Specifica il tipo di contenuto come JSON
                      },
                      redirect: 'follow'                              // Segui il reindirizzamento
                    };

                    // Esecuzione della richiesta POST utilizzando la fetch()
                    fetch(url, options)
                      .then(response => {
                        if (!response.ok) {
                          throw new Error('Errore nella richiesta POST');
                        }
                        location.reload()
                      })
                      .catch(error => {
                        console.error('Si è verificato un errore:', error);
                      });
                });

  }  
}

//L'oggetto risposta viene restituito con un campo 'status' che permette
  //all'applicazione di conoscere lo stato corrente del login di una persona,
  // if(response.status === 'connected') {
  //    Utente loggato nella web-app e su Facebook
  //                       ...
  //} else {
  //    Utente non loggato nella web-app oppure lo stato è sconosciuto
  //    document.getElementById('status').innerHTML = 'Per favore loggati nella web-app.';
  //}

//La funzione checkLoginState() viene invocata non appena viene cliccato
//il bottone: "Accedi con Facebook"
function checkLoginState() {
  FB.getLoginStatus(function(response) {
    statusChangeCallback(response);
  });
}

//Aggiungere l'SDK di Facebook per Javascript
(function(d, s, id){
                    var js, fjs = d.getElementsByTagName(s)[0];
                    if (d.getElementById(id)) {return;}
                    js = d.createElement(s); js.id = id;
                    js.src = "https://connect.facebook.net/it-IT/sdk.js";        //Impostazione lingua italiana 'it-IT'
                    fjs.parentNode.insertBefore(js, fjs);
                  }(document, 'script', 'facebook-jssdk')
);
                  
window.fbAsyncInit = function() {
  //Inizializzazione dell'SDK
  FB.init({
            appId            : '689086720098849', //ID della propria applicazione
            xfbml            : false,             //analisi DOM per trovare ed inizializzare qualsiasi plug-in social usando XFBML
            cookie           : true,
            version          : 'v19.0'            //versione del Graph API
          });

  //Una volta aver inizializzato l'SDK per Javascript, verrà chiamata
  //FB.getLoginStatus() per recuperare lo stato della persona che sta
  //attualmente visitando la web-app nel browser; verrà restituito
  //uno dei seguenti tre possibili stati:
  //
  // 1. Loggato nella web-app ('connected')
  // 2. Loggato su Facebook ma non sulla web-app ('not_authorized')
  // 3. Non loggato su Facebook, non si sa se l'Utente sia loggato sulla web-app oppure no. ('unknown')
  //
  //Questi tre casi verranno gestiti nella funzione di callback

  FB.getLoginStatus(function(response) {
    statusChangeCallback(response);
    console.log(response.authResponse.accessToken);   
  });

  FB.AppEvents.logPageView();

  //Se l'Utente è già loggato su Facebook, e probabilmente lo sarà se utilizza cookies, automaticamente verranno
  //recuperate le seguenti informazioni: nome, indirizzo email e le informazioni inerenti il profilo pubblico
  FB.login(function(response) {
            if (response.authResponse) {
                console.log('Welcome!  Fetching your information.... ');
                //console.log(response.authResponse.accessToken);

                //Recupero nome ed email tramite GET all'API di Facebook
                FB.api('/me', {fields: 'name, email'}, function(response) {
                    document.getElementById("profile").innerHTML = "Good to see you, " + response.name + ". i see your email address is " + response.email
                    console.log(JSON.stringify(response));
                });
            } else { 
                  //Se l'Utente non si è ancora loggato, apparirà una finestra di dialogo che richiederà le autorizzazioni per recuperare l'email e le informazioni
                  //riguardanti il profilo pubblico
                console.log('User cancelled login or did not fully authorize.'); }
  });

  FB.logout(function(response) {
    //Utente sloggato e ricaricamento della pagina
    location.reload();
  });
};