package com.example.db_setup;
 
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;


import java.security.Principal;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
 

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
 
import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
 
import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
 
 
import com.example.db_setup.Authentication.AuthenticatedUser;
import com.example.db_setup.Authentication.AuthenticatedUserRepository;
import com.example.db_setup.Service.OAuthUserGoogleService;
import com.example.db_setup.Language.*;
import org.springframework.web.servlet.LocaleResolver;
//MODIFICA (Deserializzazione risposta JSON)
import com.fasterxml.jackson.databind.ObjectMapper;
//FINE MODIFICA
 
@RestController
public class Controller {
 
    @Autowired
    private UserRepository userRepository;

    //GabMan 04/12
    @Autowired
    private FriendRepository friendRepository;
 
    @Autowired
    private AuthenticatedUserRepository authenticatedUserRepository;
 
    @Autowired
    private MyPasswordEncoder myPasswordEncoder;
    // Modifica 16/05/2024
    // Questo è un servizio che gestisce le operazioni relative a Google OAuth2, potrebbe non servire, è solo per testare
    @Autowired
    private OAuthUserGoogleService oAuthUserGoogleService;
 
    @Autowired
    private EmailService emailService;
 
    @Value("${recaptcha.secretkey}")
    private String recaptchaSecret;
 
    @Value("${recaptcha.url}")
    private String recaptchaServerURL;
 
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder.build();
    }
    
    @Autowired
    private RestTemplate restTemplate;
 
    //MODIFICA (10/2/2024) : gestione dei token di accesso
    private String app_token = "689086720098849|_rIns2JmCHSjLbj2in8O7M9CAWw";
    //FINE MODIFICA
 
 
    //String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{3,14}$"; // maiuscola, minuscola e numero
 
    //REGEX -- Modifica (03/02/2024) : La psw deve contenere da 8 a 16 caratteri, di cui almeno un carattere minuscolo,
    //                                 maiuscolo, un numero ed un carattere speciale
    String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,16}$"; // maiuscola, minuscola, numero e chr. speciale
    Pattern p = Pattern.compile(regex);
 
    private Integer getUserIdFromPrincipal(Principal principal) {
    // Assumi che il nome utente (principal.getName()) rappresenti l'email o l'ID dell'utente.
    // Usa UserRepository o un'altra logica per ottenere l'ID.
    User user = userRepository.findByEmail(principal.getName());
    if (user != null) {
        return user.getID(); // Supponendo che il tuo modello User abbia un metodo getID().
    }
    throw new RuntimeException("User not found");
    }

    // Registrazione
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam("name") String name,
                                            @RequestParam("surname") String surname,
                                            @RequestParam("email") String email,
                                            @RequestParam("password") String password,
                                            @RequestParam("check_password") String check_password,
                                            @RequestParam("studies") Studies studies, @CookieValue(name = "jwt", required = false) String jwt, HttpServletRequest request) {
        
        if(isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Already logged in");
        }
 
        //verifica del recaptcha
        //MODIFICA (23/2/2024) : Commento alla riga riguardante il reCAPTCHA perchè non più utilizzato
        //verifyReCAPTCHA(gRecaptchaResponse);
        
        User n = new User();
 
        // NOME -- Modifica (02/02/2024) : Possibilità di inserire più nomi separati da uno spazio
        // regex_old = "[a-zA-Z]+" , regex_new = "[a-zA-Z]+(\s[a-zA-Z]+)*"
        //if ((name.length() >= 2) && (name.length() <= 30) && (Pattern.matches("[a-zA-Z]+", name))) {
        if ((name.length() >= 2) && (name.length() <= 30) && (Pattern.matches("[a-zA-Z]+(\\s[a-zA-Z]+)*", name))) {
            n.setName(name);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name not valid");
        }
 
        // COGNOME --Modifica (02/02/2024) : Possibilità di inserire più parole separate da uno spazio ed eventualmente
        //                                  da un apostrofo
        // regex_old = "[a-zA-Z]+" , regex_new = [a-zA-Z]+(\s?[a-zA-Z]+\'?)*
        //if ((name.length() >= 2) && (surname.length() <= 30) && (Pattern.matches("[a-zA-Z]+", surname))) {
        if ((name.length() >= 2) && (surname.length() <= 30) && (Pattern.matches("[a-zA-Z]+(\\s?[a-zA-Z]+\\'?)*", surname))) {
            n.setSurname(surname);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Surname not valid");
        }
 
        // EMAIL
        if ((email.contains("@")) && (email.contains("."))) {
            User user = userRepository.findByEmail(email);
            if (user != null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utente con questa email già registrato");
            }
            n.setEmail(email);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email not valid");
        }
 
        // PASSWORD
        Matcher m = p.matcher(password);
 
        if ((password.length() >16) || (password.length() < 8) || !(m.matches())) {
 
            //return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password not valid, maiuscola, minuscola e numero, con lunghezza tra 8 e 16");
 
            //MODIFICA (05/02/2024)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password non valida! La password deve contenere almeno una lettera maiuscola, una minuscola, un numero ed un carattere speciale e deve essere lunga tra gli 8 e i 16 caratteri");
            //FINE MODIFICA
        }
 
        if (password.equals(check_password)) {
            String crypted = myPasswordEncoder.encoder().encode(password);
            n.setPassword(crypted);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Check_Password not valid");
        }
 
        // STUDIES
        n.setStudies(studies);
        n.setRegisteredWithFacebook(false);
 
        userRepository.save(n);
        Integer ID = n.getID();
 
        try {
            emailService.sendMailRegister(email, ID);
            
            //MODIFICA (03/02/2024) : Redirect
            //Modifica (18/06/2024) : Cambiato il codice per consentire il reindirizzamento
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "/login_success");    
            return new ResponseEntity<String>(headers,HttpStatus.MOVED_PERMANENTLY);
            //FINE MODIFICA
 
            //return ResponseEntity.ok("Registration completed successfully!");
 
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to confirm your registration");
        }
    }
 
    //Verifica del recaptcha
    // private void verifyReCAPTCHA(String gRecaptchaResponse) {
    //     HttpHeaders headers = new HttpHeaders();
    //     headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    
    //     MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    //     map.add("secretkey", recaptchaSecret);
    //     map.add("response", gRecaptchaResponse);
    
    //     HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
    //     ResponseEntity<String> response = restTemplate.postForEntity(recaptchaServerURL, request, String.class);
    
    //     System.out.println(response);
    // }
        
    // Autenticazione
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam("email") String email,
                                        @RequestParam("password") String password, @CookieValue(name = "jwt", required = false) String jwt, HttpServletRequest request, HttpServletResponse response) {
 
        if(isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Already logged in");
        }
 
        User user = userRepository.findByEmail(email);
 
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email not found");
        } else if (user.isRegisteredWithFacebook) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User already registered with facebook, please log in with facebook");
        }
 
        System.out.println("Utente registrato, email trovata nel database (login)");
        boolean passwordMatches = myPasswordEncoder.matches(password, user.getPassword());
        if (!passwordMatches) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password");
        }
 
        String token = generateToken(user);
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(user, token);
        authenticatedUserRepository.save(authenticatedUser);
        System.out.println("authenticatedUser creato (login)");
 
        Cookie jwtTokenCookie = new Cookie("jwt", token);
        jwtTokenCookie.setMaxAge(3600);
        response.addCookie(jwtTokenCookie);
        System.out.println("Cookie aggiunto alla risposta (login)");
        System.out.println("token_received:"+token);
 
        try {
            response.sendRedirect("/main");
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        return ResponseEntity.status(302).body("");
    }
    //MODIFICA
    @PostMapping("/login_with_facebook")
    public ResponseEntity<String> login_with_facebook(@RequestParam("email") String email,
                                                      @RequestParam("nome") String name,
                                                      @RequestParam("access_token") String tokenFb, @CookieValue(name = "jwt", required = false) String jwt, HttpServletRequest request, HttpServletResponse response) {
        // if(fb.isUserAuthenticated(tokenFb)) {
        //     // if(userRepository.findByEmail(email) == null) {
        //     //     //save user to db with email
        //     // }
        
        
        //Contattare FB  e validare il token
        //     String token = generateToken(user);
        //     AuthenticatedUser authenticatedUser = new AuthenticatedUser(user, token);
        //     authenticatedUserRepository.save(authenticatedUser);
    
        //     Cookie jwtTokenCookie = new Cookie("jwt", token);
        //     jwtTokenCookie.setMaxAge(3600);
        //     response.addCookie(jwtTokenCookie);
        // }
        System.out.println(email);
        System.out.println(tokenFb);
        System.out.println(name);
        
        //Verificare token di accesso
 
        //Invio GET presso end-point debug-token
 
        // URL dell'endpoint a cui inviare la richiesta GET
        String url = "https://graph.facebook.com/debug_token?input_token="+tokenFb+"&access_token="+app_token;
 
        // Esegue la richiesta GET e ottiene la risposta come oggetto ResponseEntity<String>
        ResponseEntity<String> login_with_facebook = restTemplate.getForEntity(url, String.class);
 
        String responseBody = null;
        boolean is_valid = false;
 
         // Verifica lo stato della risposta
         if (login_with_facebook.getStatusCode() == HttpStatus.OK) {
            // La richiesta è andata a buon fine, puoi accedere ai dati della risposta
            responseBody = login_with_facebook.getBody();
            System.out.println("Risposta ricevuta:");
            System.out.println(responseBody);
        } else {
            // Gestisci il caso in cui la richiesta non sia andata a buon fine
            System.out.println("Errore nella richiesta: " + login_with_facebook.getStatusCode());
        }
 
        //Deserializzare risposta
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("Deserializzazione JSON...");
 
        try {
            // Converti il corpo della risposta in un oggetto Java (es. MyResponseClass)
            MyResponseClass responseObj = objectMapper.readValue(responseBody, MyResponseClass.class);
    
            // Ora puoi accedere ai campi dell'oggetto responseObj
            is_valid = responseObj.getData().isIs_valid();
            //int campo2 = responseObj.getCampo2();
    
            // Esegui le operazioni desiderate con i dati della risposta
            System.out.println("is_valid: " + is_valid);
            //System.out.println("Campo2: " + campo2);
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        //Token valido?
        if (is_valid==true) {
 
            System.out.println("Token valido");
 
            //Ti sei già registrato?
            User user = userRepository.findByEmail(email);
 
            if(user != null) {
 
                //Utente esiste (mail trovata nel database)
                System.out.println("Utente già registrato (mail trovata nel database)");
 
                //Si è già registrato con Facebook?
 
                if(user.isRegisteredWithFacebook){
 
                    System.out.println("Utente registrato con Facebook");
                    //Flusso JWT
                    String token = generateToken(user);
                    AuthenticatedUser authenticatedUser = new AuthenticatedUser(user, token);
                    authenticatedUserRepository.save(authenticatedUser);
                    System.out.println("authenticatedUser correttamente creato (login_with_facebook)");
 
                    Cookie jwtTokenCookie = new Cookie("jwt", token);
                    jwtTokenCookie.setMaxAge(3600);
                    response.addCookie(jwtTokenCookie);
                    System.out.println("Cookie aggiunto alla risposta (login_with_facebook)");
 
                    try {
                        response.sendRedirect("/main");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    
                } else {
                    //Non si è registrato con Facebook
                    System.out.println("Utente non registrato con Facebook");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ti sei già registrato con email e password. Nella pagina di login, inserisci le tue credenziali.");
                }
            } else {
                //Utente non si è mai registrato
                System.out.println("Utente non si è mai registrato");
 
                //Registrazione Utente
                System.out.println("Registrazione Utente...");
                User n = new User();
                n.setName(name);
                n.setSurname("");
                n.setEmail(email);
                n.setPassword("");
                n.setRegisteredWithFacebook(true);
                n.setStudies(Studies.ALTRO);
    
                //Salvataggio
                System.out.println("Salvataggio...");
                userRepository.save(n);
                System.out.println("Salvataggio completato.");
    
                //Assegnazione ID
                Integer ID = n.getID();
    
                try {
                    emailService.sendMailRegister(email, ID);
                    //Flusso JWT
                    String token = generateToken(n);
                    AuthenticatedUser authenticatedUser = new AuthenticatedUser(n, token);
                    authenticatedUserRepository.save(authenticatedUser);
    
                    Cookie jwtTokenCookie = new Cookie("jwt", token);
                    jwtTokenCookie.setMaxAge(3600);
                    response.addCookie(jwtTokenCookie);
                    //MODIFICA (03/02/2024) : Redirect
                    try {
                        response.sendRedirect("/main");
                    } catch (IOException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to confirm your registration");
                    }
                    //FINE MODIFICA
                } catch (MessagingException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to confirm your registration");
                }
            }
        } else {
            //token non valido, utente non loggato correttamente con facebook
 
        }
 
        return ResponseEntity.status(302).body("");
 
    }
    //FINE MODIFICA
 
    public static String generateToken(User user) {
        Instant now = Instant.now();
        Instant expiration = now.plus(1, ChronoUnit.HOURS);
 
        String token = Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .claim("userId", user.getID())
                .claim("role", "user")
                .signWith(SignatureAlgorithm.HS256, "mySecretKey")
                .compact();
 
        return token;
    }
 
    // Logout
    @GetMapping("/logout")
    public ModelAndView logout(HttpServletResponse response) {
        System.out.println("-----------------LOGOUT------------------");
        Cookie jwtTokenCookie = new Cookie("jwt", null);
        jwtTokenCookie.setMaxAge(0);
        response.addCookie(jwtTokenCookie);
        System.out.println("GET logout called, token removed");
        return new ModelAndView("redirect:/login");
    }
 
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam("authToken") String authToken, HttpServletResponse response, HttpServletRequest request) {
        AuthenticatedUser authenticatedUser = authenticatedUserRepository.findByAuthToken(authToken);
        System.out.println("POST logout called, token removed");
 
        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
 
        Cookie jwtTokenCookie = new Cookie("jwt", null);
        jwtTokenCookie.setMaxAge(0);
        response.addCookie(jwtTokenCookie);
 
        // Delete JSESSIONID cookie
        Cookie jsessionidCookie = new Cookie("JSESSIONID", null);
        jsessionidCookie.setMaxAge(0);
        response.addCookie(jsessionidCookie);
 
        authenticatedUserRepository.delete(authenticatedUser);
        //Modifica 18/05/2024: Cancellazione dei cookie e del contesto di autenticazione di spring
        SecurityContextHolder.clearContext();
        HttpSession session= request.getSession(false);
        if(session != null) {
            session.invalidate();
        }
        
        return ResponseEntity.ok("Logout successful");
    }
 
 
    //Modifiche cam:Metodo per aggiornare i dati personali dell'utente
     
  @PostMapping("/updateProfile")
    public ResponseEntity<String> updateProfile(
    @CookieValue(name = "jwt", required = false) String jwt,
    @RequestParam("name") String name,
    @RequestParam("surname") String surname,
    @RequestParam("email") String email) { // Aggiunta parentesi aperta qui

    if (!isJwtValid(jwt)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
    }

    // Recupera l'ID utente dal token JWT
    Claims claims = Jwts.parser().setSigningKey("mySecretKey").parseClaimsJws(jwt).getBody();
    Integer userId = (Integer) claims.get("userId");

    // Trova l'utente nel database
    User user = userRepository.findById(userId).orElse(null);
    if (user == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    // Aggiorna i campi
    user.setName(name);
    user.setSurname(surname);
    user.setEmail(email);

    userRepository.save(user); // Salva i cambiamenti nel database
    return ResponseEntity.ok("Profile updated successfully");
    } 
// FINE MODIFICHE
 
    
    //Recupera Password
    @PostMapping("/password_reset")
    public ResponseEntity<String> resetPassword(@RequestParam("email") String email, @CookieValue(name = "jwt", required = false) String jwt, HttpServletRequest request) {
        if(isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Already logged in");
        }
 
        User user = userRepository.findByEmail(email);
 
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email not found");
        }
 
        String resetToken = generateToken(user);
        user.setResetToken(resetToken);
        userRepository.save(user);
 
        try {
            emailService.sendPasswordResetEmail(email, resetToken);
            return ResponseEntity.ok("Password reset email sent successfully");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send password reset email");
        }
 
    }
 
    @PostMapping("/password_change")
    public ResponseEntity<String> changePassword(@RequestParam("email") String email,
                                                @RequestParam("token") String resetToken,
                                                @RequestParam("newPassword") String newPassword,
                                                @RequestParam("confirmPassword") String confirmPassword, @CookieValue(name = "jwt", required = false) String jwt, HttpServletRequest request) {
 
        if(isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Already logged in");
        }
 
        User user = userRepository.findByEmail(email);
 
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email not found");
        }
 
        if (!resetToken.equals(user.getResetToken())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid reset token");
        }
 
        Matcher m = p.matcher(newPassword);
 
        if ((newPassword.length() >= 15) || (newPassword.length() <= 2) || !(m.matches())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password not valid");
        }
 
        if (newPassword.equals(confirmPassword)) {
            String cryptedPassword = myPasswordEncoder.encoder().encode(newPassword);
            user.setPassword(cryptedPassword);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Check_Password not valid");
        }
 
        user.setResetToken(null);
        userRepository.save(user);
 
        return ResponseEntity.ok("Password change successful");
    }
 
    // // ID per il task 5
    // @GetMapping("/get_ID")
    // public Integer getID(@RequestParam("email") String email, @RequestParam("password") String password){
        
    //     User user = userRepository.findByEmail(email);
 
    //     if (user == null) {
    //         return -1;
    //     }
 
    //     boolean passwordMatches = myPasswordEncoder.matches(password, user.password);
    //     if (!passwordMatches) {
    //         return -1;
    //     }
 
    //     Integer ID= user.ID;
 
    //     return ID;
    // }
 
    /* GET PER LE VIEW */
 
    public boolean isJwtValid(String jwt) {
        try {
            Claims c = Jwts.parser().setSigningKey("mySecretKey").parseClaimsJws(jwt).getBody();
 
            if((new Date()).before(c.getExpiration())) {
                return true;
            }
        } catch(Exception e) {
            System.err.println(e);
        }
 
        return false;
    }
 
    @PostMapping("/validateToken")
    public ResponseEntity<Boolean> checkValidityToken( @RequestParam("jwt") String jwt) {
        if(isJwtValid(jwt)) return ResponseEntity.ok(true);
 
        return ResponseEntity.ok(false);
    }
 
    @GetMapping("/register")
    public ModelAndView showRegistrationForm(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
        if(isJwtValid(jwt)) return new ModelAndView("redirect:/main");
 
        return new ModelAndView("register_new");
    }
 
    //MODIFICA (03/02/2024) : Feedback registrazione avvenuta con successo + redirect alla pagina di /login
    @GetMapping("/login_success")
    public ModelAndView showLoginSuccesForm(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
        if(isJwtValid(jwt)) return new ModelAndView("redirect:/main");
        return new ModelAndView("login_success");
    }
    
    //MODIFICA (18/02/2024) : Aggiunta menù
    @GetMapping("/menu")
    public ModelAndView showMenuForm(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
 
        System.out.println("GET (/menu)");
        if(isJwtValid(jwt)) return new ModelAndView("redirect:/login");
 
        return new ModelAndView("menu_new");
    }
    //FINE MODIFICA
 
    @GetMapping("/login")
    public ModelAndView showLoginForm(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
        if(isJwtValid(jwt)) return new ModelAndView("redirect:/main");
 
        return new ModelAndView("login_new");
    }
 
    @GetMapping("/students_list")
    public List<User> getAllStudents() {
        return userRepository.findAll();
    }
 
    @GetMapping("/students_list/{ID}")
    @ResponseBody
    public User getStudent(@PathVariable String ID) {
        return userRepository.findByID(Integer.parseInt(ID));
    }
 
    
    @GetMapping("/password_reset")
    public ModelAndView showResetForm(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
        if(isJwtValid(jwt)) return new ModelAndView("redirect:/main");
        
        return new ModelAndView("password_reset_new");
    }
 
    
    @GetMapping("/password_change")
    public ModelAndView showChangeForm(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
        if(isJwtValid(jwt)) return new ModelAndView("redirect:/main");
 
        return new ModelAndView("password_change");
    }
 
    @GetMapping("/mail_register")
    public ModelAndView showMailForm(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
        if(isJwtValid(jwt)) return new ModelAndView("redirect:/main");
 
        return new ModelAndView("mail_register");
    }
    //Modifica 16/05/2024: Aggiunta login con Google
 
    // Questo metodo reindirizza l'utente alla pagina di autorizzazione di Google per il login
    @GetMapping("/loginWithGoogle")
    public void loginWithGoogle(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/google");
    }
    
    @GetMapping("/checkService")
    @ResponseBody
    public String checkService() {
        return (oAuthUserGoogleService != null) ? "Service is defined" : "Service is not defined";
    }
 
    @GetMapping("/checkSession")
    public ResponseEntity<String> checkSession(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session != null && !session.isNew()) {
        return ResponseEntity.ok("Session is active");
    } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session is not active");
    }
}
    @GetMapping("/test_prova")
    @ResponseBody
    public String test() {
        return "test T23";
    }
 
    @PostMapping("/changeLanguage")
    public String changeLanguage(HttpServletRequest request, @RequestParam("lang") String lang, RedirectAttributes redirectAttributes) {
    LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
    if (localeResolver != null) {
        localeResolver.setLocale(request, null, Locale.forLanguageTag(lang));
    }
    // Redirect back to the referring page, or to a default page
    String referer = request.getHeader("Referer");
    return "redirect:" + (referer != null ? referer : "/");
}
//cami 06/12
  @PostMapping("/updateAvatar")
    public ResponseEntity<String> updateAvatar(
    @CookieValue(name = "jwt", required = false) String jwt,
    @RequestParam("avatar") String avatar) { // Modifica da "avatar" a "avatarPath"
    try {
        // Verifica il token JWT
        if (jwt == null || jwt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        // Decodifica il token JWT per ottenere l'ID utente
        byte[] decodedUserObj = Base64.getDecoder().decode(jwt.split("\\.")[1]);
        String decodedUserJson = new String(decodedUserObj, StandardCharsets.UTF_8);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> userData = mapper.readValue(decodedUserJson, Map.class);
        String userId = userData.get("userId").toString();

        // Recupera l'utente dal database
        Optional<User> optionalUser = userRepository.findById(Integer.parseInt(userId));

        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Aggiorna l'avatar dell'utente
        User user = optionalUser.get();
        user.setAvatar(avatar); // Salva il nuovo avatar
        userRepository.updateAvatar(Integer.parseInt(userId), avatar);

        return ResponseEntity.ok("Avatar updated successfully!");
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating avatar.");
    }
    }
    @GetMapping("/getAvatar")
    public ResponseEntity<String> getAvatar(@CookieValue(name = "jwt", required = false) String jwt) {
    try {
        // Verifica se il token JWT è presente
        if (jwt == null || jwt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        // Decodifica il token JWT per ottenere l'ID utente
        byte[] decodedUserObj = Base64.getDecoder().decode(jwt.split("\\.")[1]);
        String decodedUserJson = new String(decodedUserObj, StandardCharsets.UTF_8);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> userData = mapper.readValue(decodedUserJson, Map.class);
        String userId = userData.get("userId").toString();

        // Recupera l'utente dal database
        Optional<User> optionalUser = userRepository.findById(Integer.parseInt(userId));
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Ottieni l'avatar
        User user = optionalUser.get();
        String avatarPath = user.getAvatar();

        // Verifica se l'avatar è presente
        if (avatarPath == null || avatarPath.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No avatar found");
        }

        // Restituisci il percorso dell'avatar
        return ResponseEntity.ok(avatarPath);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving avatar");
    }
    }



 
/*Modifiche GabMan 29/11/2024 : Gestione biografia in sezione separata dal profilo  */

    @GetMapping("/getBiography")
    public ResponseEntity<Map<String, String>> getBiography(
    @CookieValue(name = "jwt", required = false) String jwt) {
    try {
        // Verifica il token JWT
        if (jwt == null || jwt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Decodifica il token JWT per ottenere l'ID utente
        byte[] decodedUserObj = Base64.getDecoder().decode(jwt.split("\\.")[1]);
        String decodedUserJson = new String(decodedUserObj, StandardCharsets.UTF_8);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> userData = mapper.readValue(decodedUserJson, Map.class);
        String userId = userData.get("userId").toString();

        // Recupera l'utente dal database
        Optional<User> optionalUser = userRepository.findById(Integer.parseInt(userId));

        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Recupera la biografia
        User user = optionalUser.get();
        Map<String, String> response = new HashMap<>();
        response.put("biography", user.getBiography());

        return ResponseEntity.ok(response);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    }

    @PostMapping("/updateBiography")
    public ResponseEntity<String> updateBiography(
        @CookieValue(name = "jwt", required = false) String jwt,
        //@RequestParam("userId") Integer userId,
        @RequestParam("biography") String biography) {
    try {
        // Verifica il token JWT
        if (jwt == null || jwt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        // Decodifica il token JWT per ottenere l'ID utente
        byte[] decodedUserObj = Base64.getDecoder().decode(jwt.split("\\.")[1]);
        String decodedUserJson = new String(decodedUserObj, StandardCharsets.UTF_8);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> userData = mapper.readValue(decodedUserJson, Map.class);
        String userId = userData.get("userId").toString();

        // Recupera l'utente dal database
       Optional<User> optionalUser = userRepository.findById(Integer.parseInt(userId));

        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Aggiorna la biografia
        User user = optionalUser.get();
        user.setBiography(biography);
        userRepository.save(user);

        return ResponseEntity.ok("Biography updated successfully!");
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating biography.");
    }
    }

    // by GabMan: 03/24 - Endpoint per ottenere la lista degli amici
    @GetMapping("/getFriendlist")
    public ResponseEntity<List<Map<String, String>>> getFriendlist(@CookieValue(name = "jwt", required = false) String jwt) {
    try {
        // Verifica che il token JWT esista
        if (jwt == null || jwt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Decodifica del JWT per ottenere l'ID utente
        byte[] decodedUserObj = Base64.getDecoder().decode(jwt.split("\\.")[1]);
        String decodedUserJson = new String(decodedUserObj, StandardCharsets.UTF_8);

        ObjectMapper mapper = new ObjectMapper();
        @SuppressWarnings("unchecked")
        Map<String, Object> map = mapper.readValue(decodedUserJson, Map.class);
        int userId = Integer.parseInt(map.get("userId").toString());

        // Recupera la lista degli amici con i dettagli (nickname e avatar) dal repository
        List<Map<String, Object>> friends = friendRepository.findFriendDetailsByUserId(userId);

        // Trasforma i dati degli amici in una lista di mappe da restituire come JSON
        List<Map<String, String>> friendListResponse = new ArrayList<>();
        for (Map<String, Object> friend : friends) {
            Map<String, String> friendData = new HashMap<>();
            friendData.put("nickname", friend.get("nickname").toString());
            friendData.put("avatar", friend.get("avatar").toString()); // Recuperato dalla tabella students
            friendData.put("friendId", friend.get("friendId").toString());
            friendListResponse.add(friendData);
        }

        return ResponseEntity.ok(friendListResponse);

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    }


    private Integer extractUserIdFromJwt(String jwt) {
        if (jwt == null || jwt.isEmpty()) {
            return null;
        }

        try {
            // Decodifica la parte payload del JWT (seconda parte, separata da '.')
            String[] parts = jwt.split("\\.");
            if (parts.length < 2) {
                return null;
            }

            byte[] decodedBytes = Base64.getDecoder().decode(parts[1]);
            String payload = new String(decodedBytes, StandardCharsets.UTF_8);

            // Usa una libreria come Jackson per convertire il JSON in una mappa
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> payloadMap = objectMapper.readValue(payload, Map.class);

            // Assumi che il payload contenga una chiave "userId"
            if (payloadMap.containsKey("userId")) {
                return Integer.parseInt(payloadMap.get("userId").toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

  

  
     
    //by GabMan: Endpoint per aggiungere un amico
    @PostMapping("/addFriend")
    public ResponseEntity<String> addFriend(
        @CookieValue(name = "jwt", required = false) String jwt,
        @RequestParam Integer friendId) {
        try {
            // Estrai l'ID dell'utente dal token JWT
            Integer userId = extractUserIdFromJwt(jwt);
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token non valido.");
            }
    
            // Crea la chiave composta FriendId
            FriendId friendIdObj = new FriendId(userId, friendId);
    
            if (friendRepository.customExistsById(userId, friendId)) {
            return ResponseEntity.badRequest().body("Amicizia già esistente.");
            }

    
            // Aggiungi l'amico (usando il metodo personalizzato nel repository)
            friendRepository.addFriend(userId, friendId);
            return ResponseEntity.ok("Amico aggiunto con successo.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore interno del server.");
        }
    }
    

    @PostMapping("/removeFriend")
    public ResponseEntity<String> removeFriend(
    @CookieValue(name = "jwt", required = false) String jwt,
    @RequestParam Integer friendId) {
    try {
        if (jwt == null || jwt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token non valido o mancante.");
        }

        // Estrai l'ID utente dal token JWT
        Integer userId = extractUserIdFromJwt(jwt);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ID utente non valido.");
        }

        // Crea un oggetto FriendId
        FriendId friendIdObj = new FriendId(userId, friendId);

       if (!friendRepository.customExistsById(userId, friendId)) {
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Amicizia non trovata.");
    }


        // Rimuovi l'amicizia dal database
    friendRepository.deleteFriend(userId, friendId);

        
        return ResponseEntity.ok("Amico rimosso con successo.");
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore interno del server.");
    }
    }


    







    
    //cami 02/12--> modifica profilo
    @GetMapping("/getUserInfo")
    public ResponseEntity<Map<String, String>> getUserInfo(@CookieValue(name = "jwt", required = false) String jwt) {
    try {
        // Verifica il token JWT
        if (jwt == null || jwt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Decodifica il token JWT per ottenere l'ID utente
        byte[] decodedUserObj = Base64.getDecoder().decode(jwt.split("\\.")[1]);
        String decodedUserJson = new String(decodedUserObj, StandardCharsets.UTF_8);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> userData = mapper.readValue(decodedUserJson, Map.class);
        String userId = userData.get("userId").toString();

        // Recupera l'utente dal database
        Optional<User> optionalUser = userRepository.findById(Integer.parseInt(userId));

        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Prepara la risposta con nome, cognome e nickname
        User user = optionalUser.get();
        Map<String, String> response = new HashMap<>();
        response.put("name", user.getName());
        response.put("surname", user.getSurname());
        response.put("nickname", user.getNickname()); 

        return ResponseEntity.ok(response);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    }
    //GabMan 09/12 --> updateUserInfo
    @PostMapping("/updateUserInfo")
    public ResponseEntity<String> updateUserInfo(
            @CookieValue(name = "jwt", required = false) String jwt,
            @RequestParam("name") String name,
            @RequestParam("surname") String surname,
            @RequestParam("nickname") String nickname) {
        try {
            // Verifica il token JWT
            if (jwt == null || jwt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }

            // Decodifica il token JWT per ottenere l'ID utente
            byte[] decodedUserObj = Base64.getDecoder().decode(jwt.split("\\.")[1]);
            String decodedUserJson = new String(decodedUserObj, StandardCharsets.UTF_8);

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> userData = mapper.readValue(decodedUserJson, Map.class);
            String userId = userData.get("userId").toString();

            // Recupera l'utente dal database
            Optional<User> optionalUser = userRepository.findById(Integer.parseInt(userId));

            if (!optionalUser.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            // Aggiorna i dati dell'utente
            User user = optionalUser.get();
            user.setName(name);
            user.setSurname(surname);
            user.setNickname(nickname);
            userRepository.save(user); // Salva le modifiche nel database

            return ResponseEntity.ok("User information updated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }


    //cami 07/12
    @GetMapping("/searchFriend")
    public ResponseEntity<Map<String, String>> searchFriend(@RequestParam String identifier) {
    try {
        // Cerca l'utente tramite nickname o ID
        User user = userRepository.findByNickname(identifier);
        if (user == null) {
            try {
                user = userRepository.findByID(Integer.parseInt(identifier));
            } catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Restituisci i dettagli dell'utente trovato
        Map<String, String> userDetails = new HashMap<>();
        userDetails.put("id", user.getID().toString());
        userDetails.put("nickname", user.getNickname());
        return ResponseEntity.ok(userDetails);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    }

    /*
    //10/12 Update profile picture
    @PostMapping("/updateProfilePicture")
    public ResponseEntity<Boolean> updateProfilePicture(
    @RequestParam("userId") Integer userId,
    @RequestParam("image") String base64Image) {
    try {
        // Decodifica l'immagine da Base64
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);

        // Salva l'immagine come file o direttamente nel database
        String fileName = "user_" + userId + ".jpg";
        Path filePath = Paths.get("uploads/profile_pictures/" + fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, imageBytes);

        // Aggiorna l'URL dell'immagine nel database (esempio)
        String imageUrl = "/uploads/profile_pictures/" + fileName;
        studentService.updateProfilePicture(userId, imageUrl);

        return ResponseEntity.ok(true);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
    }
    */
}


    












 
