

package com.g2.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.g2.Interfaces.ServiceManager;
import com.g2.Model.User;
import com.g2.Model.UserProfile;

@Service
public class UserProfileService{
    private final ServiceManager serviceManager;

    @Autowired
    public UserProfileService(RestTemplate restTemplate){
        this.serviceManager = new ServiceManager(restTemplate);
    }

    public String getProfileBio(int playerID){
         // Mi prendo l'id del giocatore, così forse carico la foto e la bio che già ci sono
        int userId = playerID;

        // Mi prendo prima tutti gli utenti
        @SuppressWarnings("unchecked")
        List<User> users = (List<com.g2.Model.User>)serviceManager.handleRequest("T23", "GetUsers");

        // Mi prendo l'utente che mi interessa con l'id e la sua bio
        User user = users.stream().filter(u -> u.getId() == userId).findFirst().orElseThrow(() -> new RuntimeException("User not found"));
        String bio = user.getUserProfile().getBio();

        return bio;
    }

   //Prendo tutte le immagini fra le quali posso selezionare la mia
   public List<String> getAllProfilePictures(){

   // Mi prendo le foto
   /*
    List<String> list_images = new ArrayList<>();
    String directoryPath = "profileImages";
    URL resource = getClass().getClassLoader().getResource(directoryPath);
    if (resource == null) {
        System.err.println("Directory non trovata nel class path");
        return list_images;
    }
    File directory = new File(resource.getFile());

    // Verifica se il percorso esiste ed è una directory
    if (!directory.exists() || !directory.isDirectory()) {
        System.err.println("Percorso non valido o non è una directory: " + directoryPath);
        directory=null; // Oppure una lista vuota o lancia un'eccezione, a seconda delle tue esigenze
    }

    // Crea un filtro per accettare solo file immagine (ad esempio, .jpg, .png, .gif)
    FilenameFilter imageFilter = (dir, name) -> {
        String lowercaseName = name.toLowerCase();
        return lowercaseName.endsWith(".jpg") || lowercaseName.endsWith(".png") || lowercaseName.endsWith(".gif");
    };

    // Ottieni la lista dei nomi dei file che corrispondono al filtro
    String[] imageNamesArray = null;
    if(directory!=null){
        imageNamesArray = directory.list(imageFilter);
    }

    // Converti l'array in una lista (opzionale, ma spesso più utile)
    if (imageNamesArray != null) {
        List<String> list = new ArrayList<>(Arrays.asList(imageNamesArray));
        list_images = list;
    } else {
    System.err.println("Nessun file immagine trovato nella directory: " + resource);
        list_images=null; // Oppure una lista vuota o lancia un'eccezione, a secondo delle tue esigenze
    }
    */

    List<String> list_images = new ArrayList<>();
    list_images.add("default.png");
    list_images.add("men-1.png");
    list_images.add("men-2.png");
    list_images.add("men-3.png");
    list_images.add("men-4.png");
    list_images.add("women-1.png");
    list_images.add("women-2.png");
    list_images.add("women-3.png");
    list_images.add("women-4.png");

    return list_images;
}

public String getProfilePicture(int playerID){
    // Mi prendo l'id del giocatore, così carico la foto e la bio che già ci sono
   int userId = playerID;

   // Mi prendo prima tutti gli utenti
   @SuppressWarnings("unchecked")
   List<User> users = (List<com.g2.Model.User>)serviceManager.handleRequest("T23", "GetUsers");

   // Mi prendo l'utente che mi interessa con l'id
   User user = users.stream().filter(u -> u.getId() == userId).findFirst().orElseThrow(() -> new RuntimeException("User not found"));

   List<String> list_images=this.getAllProfilePictures();

    //Verifico la validità del path
    Boolean propicvalid = false;
    for (int i=0;i<list_images.size();i++){
        if (user.getUserProfile().getProfilePicturePath().equals(list_images.get(i))){
            propicvalid=true;
            break;
        }
    }

    //se lo trovo restituisco l'immagine
    if(propicvalid){
        return user.getUserProfile().getProfilePicturePath();
    }

   return null;
}

public Integer getProfileID(int playerID){

    // Mi prendo l'id del giocatore
    int userId = playerID;

    // Mi prendo prima tutti gli utenti
    @SuppressWarnings("unchecked")
    List<User> users = (List<com.g2.Model.User>)serviceManager.handleRequest("T23", "GetUsers");

    // Mi prendo l'utente che mi interessa con l'id
    User user = users.stream().filter(u -> u.getId() == userId).findFirst().orElseThrow(() -> new RuntimeException("User not found"));

    return user.getUserProfile().getID();

    }

public List<Integer> getFollowingList(int playerID){
    // Mi prendo l'id del giocatore
    int userId = playerID;

    // Mi prendo prima tutti gli utenti
    @SuppressWarnings("unchecked")
    List<User> users = (List<com.g2.Model.User>)serviceManager.handleRequest("T23", "GetUsers");

    // Mi prendo l'utente che mi interessa con l'id
    User user = users.stream().filter(u -> u.getId() == userId).findFirst().orElseThrow(() -> new RuntimeException("User not found"));

    System.out.println(user.toString());

    return user.getUserProfile().getFollowingList();

    }

public List<Integer> getFollowersList(int playerID){
    // Mi prendo l'id del giocatore
    int userId = playerID;

    // Mi prendo prima tutti gli utenti
    @SuppressWarnings("unchecked")
    List<User> users = (List<com.g2.Model.User>)serviceManager.handleRequest("T23", "GetUsers");

    // Mi prendo l'utente che mi interessa con l'id
    User user = users.stream().filter(u -> u.getId() == userId).findFirst().orElseThrow(() -> new RuntimeException("User not found"));

    return user.getUserProfile().getFollowersList();

    }

}

