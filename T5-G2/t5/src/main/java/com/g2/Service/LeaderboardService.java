package com.g2.Service;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g2.Interfaces.ServiceManager;
import com.g2.Model.PlayerDTO;
import com.g2.Model.Player;
import com.g2.Model.User;

@Service
public class LeaderboardService {
    private final ServiceManager serviceManager;
    private List<PlayerDTO> lista_PlayerDTO;
    
    @Autowired
    public LeaderboardService(ServiceManager serviceManager){
        this.serviceManager = serviceManager;
        
    }


    private List<Player> getPlayers(){
        return (List<Player>) serviceManager.handleRequest("T4","GetAllPlayers");
    }
    private List<User> getUsers(){
        return (List<User>) serviceManager.handleRequest("T23", "GetUsers", null);
    }

    public void getList(){
        List<Player> lista_player=getPlayers();
        List<User> lista_user= getUsers();
        try {
            // Crea una mappa degli utenti per ID
            Map<Long, User> userMap = lista_user.stream()
                .collect(Collectors.toMap(User::getId, user -> user));

            // Unisci le due liste creando una lista di PlayerDTO
            lista_PlayerDTO= lista_player.stream().map(player -> {
                User user = userMap.get((long)player.getID()); // Assumendo che Player abbia il campo `userId`

                // Se l'utente esiste, crea un PlayerDTO
                if (user != null) {
                    return new PlayerDTO(user.getId(),user.getName(), user.getSurname(), player.getPoints(), player.getGamesWon());
                } else {
                    return null; // Se non c'è un utente corrispondente, ritorna null
                }
            })
            .collect(Collectors.toList()); // Raccoglie i risultati in una lista
        } catch (Exception e) {
            e.printStackTrace(); // Log dell'errore
            throw new RuntimeException("Errore nel recupero della leaderboard", e);
        }
    }


    public List<PlayerDTO> getLeaderboardFilteredByPoints(){

        return lista_PlayerDTO.stream()
        .filter(playerDTO -> playerDTO.getPoints() > 0) // Filtra per punti > 0
        .sorted(Comparator.comparingInt(PlayerDTO::getPoints).reversed()) // Ordina per punti decrescenti
        .collect(Collectors.toList()); // Raccoglie i risultati in una lista
        }

    public int getPlayerRankByPoints(long playerId) {
        List<PlayerDTO> sortedLeaderboard = getLeaderboardFilteredByPoints();
    
        // Cerca la posizione del player con l'ID specificato
  

        return java.util.stream.IntStream.range(0, sortedLeaderboard.size())
            .filter(i -> sortedLeaderboard.get(i).getID() == playerId) // Trova l'indice corrispondente
            .findFirst() // Restituisce un OptionalInt
            .orElse(-1) + 1; // Se non trovato, ritorna -1; altrimenti, aggiunge 1 per la posizione 1-based OPPURE GENERARE ECCEZIONE
    }
    
    public List<PlayerDTO> getLeaderboardFilteredByGames(){

        return  lista_PlayerDTO.stream()
        .filter(playerDTO -> playerDTO.getGamesWon() > 0) // Gestisci anche null
        .sorted(Comparator.comparingInt(PlayerDTO::getGamesWon).reversed()) // Ordina per partite giocate decrescenti
        .collect(Collectors.toList());


    }

    public int getPlayerRankByGames(long playerId){

        List<PlayerDTO> sortedLeaderboard = getLeaderboardFilteredByGames();

        return java.util.stream.IntStream.range(0, sortedLeaderboard.size())
            .filter(i -> sortedLeaderboard.get(i).getID() == playerId) // Trova l'indice corrispondente
            .findFirst() // Restituisce un OptionalInt
            .orElse(-1) + 1; // Se non trovato, ritorna -1; altrimenti, aggiunge 1 per la posizione 1-based
    }

}
