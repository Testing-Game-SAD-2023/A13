# Refactoring security microservizio T1
Nella vecchia versione il controller presentava l'input del jwt per effettuare un controllo di sicurezza ridondante

```java
@PostMapping("/URI/RICHIESTA") ​
public ResponseEntity<?> aControllerMethod(aType anInputParameter, @CookieValue(name = "jwt", required = false) String jwt) { ​

    // ... ​

    return aService.aServiceMethod(anInputParameter, jwt); ​

} 
```
Il service, invece, gestiva il controllo della validità del token e generava la risposta HTTP. 
```java
public ResponseEntity<?> aServiceMethod(aType anInputParameter, String jwt) { ​

    if (!jwtService.isJwtValid(jwt)) { ​

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unauthorized");​

    } ​

    // ... logica del service ... ​

    return ResponseEntity.ok("...");​

}
```
Ogni metodo, dunque, effettuava un controllo ridondante del JWT, presentando l'input 
```java
@CookieValue(name = "jwt", required = false) String jwt
```
Tale controllo era delegato al Service, il quale dovrebbe occuparsi della sola logica di businness.
I metodi del service restituivano risposte  ResponseEntity<?>, mentre dovrebbe essere compito del Controller generare la risposta HTTP in base all’occorrenza.

Ora il controller prende in input solo gli argomenti della richiesta, per poi passali al Service, e determina quale risposta HTTP ResponseEntity<?> restituire, con l’aiuto del GlobalExceptionHandler per le risposte HTTP in caso di eccezioni (tra cui UnauthorizedException).
```java
@PostMapping("/URI/RICHIESTA") ​

public ResponseEntity<?> aControllerMethod(aType anInputParameter) { ​

    // ... ​

    aService.aServiceMethod(anInputParameter); ​

    // ... ​

    return ResponseEntity.ok("...");​

}
```
Il service effettua solo la logica di business del sistema.  
```java
public void aServiceMethod(aType anInputParameter) { ​

    // ... logica del service ... ​

}
```
Il GlobalExceptionHandler cattura a monte, automaticamente, l'eccezione UnauthorizedException sollevata da AuthTokenFilter.
```java
@ExceptionHandler(UnauthorizedException.class)​

public ResponseEntity<String> handleUnauthorized(UnauthorizedException ex) {​

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());​

}
```