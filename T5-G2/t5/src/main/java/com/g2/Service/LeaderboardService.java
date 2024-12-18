package com.g2.Service; 
import java.util.ArrayList;
import java.util.Comparator; 
import java.util.List; 
import java.util.Map; 
import java.util.stream.Collectors; 
 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonTypeInfo.None;
import com.g2.Interfaces.ServiceManager; 
import com.g2.Model.PlayerDTO; 
import com.g2.Model.Player; 
import com.g2.Model.User; 
 
@Service 
public class LeaderboardService { 
    private final ServiceManager serviceManager; 
    private List<PlayerDTO> sortedLeaderboardByPoints; 
    private List<PlayerDTO> sortedLeaderboardByGames; 
 
     
    @Autowired 
    public LeaderboardService(ServiceManager serviceManager){ 
        this.serviceManager = serviceManager; 
         
    } 
 
     
 
    public List<PlayerDTO> getSortedLeaderboardByPoints() { 
        return sortedLeaderboardByPoints; 
    } 
 
 
 
    public List<PlayerDTO> getSortedLeaderboardByGames() { 
        return sortedLeaderboardByGames; 
    } 
 
 
 
    //richiama servizio per prelevare tutti i Player da T4 
    private List<Player> getPlayers(){ 
        return (List<Player>) serviceManager.handleRequest("T4","GetAllPlayers"); 
    } 
    //richiama servizio per prelevare tutti gli User da T23 
    private List<User> getUsers(){ 
        return (List<User>) serviceManager.handleRequest("T23", "GetUsers", null); 
    } 
 
    //funzione di utilità per filtrare lista PlayerDTO per Punti 
    public List<PlayerDTO> getLeaderboardFilteredByPoints(List<PlayerDTO> lista_PlayerDTO){ 
 
        return lista_PlayerDTO.stream() 
        .filter(playerDTO -> playerDTO.getPoints() > 0) // Filtra per punti > 0 
        .sorted(Comparator.comparingInt(PlayerDTO::getPoints).reversed()) // Ordina per punti decrescenti 
        .collect(Collectors.toList()); // Raccoglie i risultati in una lista 
        } 
 
    //funzione di utilità per filtrare lista PlayerDTO per PartiteVinte    
    public List<PlayerDTO> getLeaderboardFilteredByGames(List<PlayerDTO> lista_PlayerDTO){ 
 
        return  lista_PlayerDTO.stream() 
        .filter(playerDTO -> playerDTO.getGamesWon() > 0) // Gestisci anche null 
        .sorted(Comparator.comparingInt(PlayerDTO::getGamesWon).reversed()) // Ordina per partite giocate decrescenti 
        .collect(Collectors.toList()); 
 
 
    } 
 
    //funzione per ottenere lista completa di Utenti e per ottenere le classifiche 
    public void getList(){ 
        List<Player> lista_player=getPlayers(); 
        List<User> lista_user= getUsers(); 
        if(lista_player==null){
        
            lista_player = new ArrayList<Player>();
        }
        try { 
            // Crea una mappa degli utenti per ID 
            Map<Long, User> userMap = lista_user.stream() 
                .collect(Collectors.toMap(User::getId, user -> user)); 
 
            // Unisci le due liste creando una lista di PlayerDTO 
            List<PlayerDTO> lista_PlayerDTO= lista_player.stream().map(player -> { 
                User user = userMap.get((long)player.getID()); // Assumendo che Player abbia il campo userId 
 
                // Se l'utente esiste, crea un PlayerDTO 
                if (user != null) { 
                    return new PlayerDTO(user.getId(),user.getName(), user.getSurname(), player.getPoints(), player.getGamesWon()); 
                } else { 
                    return null; // Se non c'è un utente corrispondente, ritorna null 
                } 
            }) 
            .collect(Collectors.toList()); // Raccoglie i risultati in una lista 
 
            //filtro per Punti 
            sortedLeaderboardByPoints = getLeaderboardFilteredByPoints(lista_PlayerDTO); 
 
            //filtro per Partite Vinte 
            sortedLeaderboardByGames = getLeaderboardFilteredByGames(lista_PlayerDTO); 
 
 
 
        } catch (Exception e) { 
            e.printStackTrace(); // Log dell'errore 
            throw new RuntimeException("Errore nel recupero della leaderboard", e); 
        } 
    } 

    public PlayerDTO getPlayerRank(long playerId){ 
 
         
        // Cerca la posizione del player con l'ID specificato 
        int rankByGames =  java.util.stream.IntStream.range(0, sortedLeaderboardByGames.size()) 
            .filter(i -> sortedLeaderboardByGames.get(i).getID() == playerId) // Trova l'indice corrispondente 
            .findFirst() // Restituisce un OptionalInt 
            .orElse(-1) ; // Se non trovato, ritorna -1 
         
        int rankByPoints = java.util.stream.IntStream.range(0, sortedLeaderboardByPoints.size()) 
            .filter(i -> sortedLeaderboardByPoints.get(i).getID() == playerId) // Trova l'indice corrispondente 
            .findFirst() // Restituisce un OptionalInt 
            .orElse(-1) ; // Se non trovato, ritorna -1 
         
 
 
        //cerco l'utente corrente tra la lista di utenti, aggiungo informazioni sulla posizione e lo restituisco 
        
        PlayerDTO player_current = new PlayerDTO(); 
 
        //caso in cui l'user autenticato non è presente in nessuna delle due classifiche 
        if (rankByPoints == -1 && rankByGames == -1) { 
            return new PlayerDTO(playerId,-1,-1); 
        } 
 
        //casi in cui l'user autenticato è presente soltanto in una delle due : 
        if (rankByGames != -1) { 
            player_current = sortedLeaderboardByGames.get(rankByGames); 
        } else if (rankByPoints != -1) { 
            player_current = sortedLeaderboardByPoints.get(rankByPoints); 
        } 
 
        if (player_current != null) { 
            //aggiungo informazioni sulle posizioni, se diverse da 1 
            // +1 necessario per notazione 1-based 
            player_current.setRankByGames(rankByGames != -1 ? rankByGames + 1 : -1); 
            player_current.setRankByPoints(rankByPoints != -1 ? rankByPoints + 1 : -1); 
        } 
        return player_current; 
         
    } 
 
}
    //restituisce informazioni sul player autenticato (posizione per GamesWin e posizione per Points) 
    //se l'utente corrente non è in classifica restituisce -1 
