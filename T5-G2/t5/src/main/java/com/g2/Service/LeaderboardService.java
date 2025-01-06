package com.g2.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.g2.Model.Classifica;
import com.g2.Model.Player;
import com.g2.Service.PlayerComparatorService;
import com.g2.Service.LeaderboardDatabaseService;

@Service
public class LeaderboardService {

    @Autowired
    private LeaderboardDatabaseService leaderboardDatabaseService;

    public List<Player> getPlayerData(Classifica lead) {
        // Supponendo che `Player` abbia un costruttore che accetti questi parametri
        PlayerComparatorService comparatorService = new PlayerComparatorService();
        List<Player> players = getLeaderboard();

        if (!players.isEmpty() && lead.getSortOrder() != null && !lead.getSortOrder().isEmpty()) {
            Comparator<Player> comparator = comparatorService.getComparator(lead.getSortOrder());
            if (comparator != null) {
                players.sort(comparator);
            }
        }

        return players;
    }


        // Metodo per convertire la lista di Player in una lista di Mappe <string, object> necessario al passaggio dei dati con thymeleaf
    public List<Map<String, Object>> getPlayersToMapList(Classifica lead, String SearchFilter) {
        List<Map<String, Object>> playersMapList = new ArrayList<>();
        int position = 1; // Inizia da 1 per la posizione del primo giocatore
        for (Player player : lead.getPlayers()) {
            Map<String, Object> playerMap = new HashMap<>();
            playerMap.put("Mail", player.getMail());
            playerMap.put("totalMatches", player.getMatches());
            playerMap.put("wins", player.getWins());
            playerMap.put("totalScore", player.getTotalScore());
            playerMap.put("position", position); // Aggiunge la posizione attuale del giocatore alla mappa
            playersMapList.add(playerMap);
            position++; // Incrementa la posizione per il prossimo giocatore
        }
        // Filtra i giocatori prima di aggiungerli alla lista di mappe
        if (SearchFilter != null  && !SearchFilter.isEmpty()) {
            playersMapList = playersMapList.stream()
                                           .filter(playerMap -> playerMap.get("Mail").equals(SearchFilter))
                                           .collect(Collectors.toList());
            //Se é stata effettuata una ricerca, filtro gli utenti anche nella leaderboard
            //(questo secondo filtro puó sembrare inutile, ma serve a ricalcolare il nuovo totalpages della classifica)
            List<Player> filteredPlayers = lead.getPlayers().stream()
                        .filter(player -> player.getMail().equals(SearchFilter))
                        .collect(Collectors.toList());
            lead.setPlayers(filteredPlayers);
        }
        return playersMapList;
        }

    //metodo utile per ottenere la lista dei giocatori che devono essere mostrati nella pagina
    public List<Map<String, Object>> getPlayersInPage(Classifica lead, String SearchFilter) {
        //Trasformo la lista di tutti i giocatori in una lista di mappe, operazione enecessaria per poter lavorare con thymeleaf
        List<Map<String, Object>> mappedPlayers = getPlayersToMapList(lead, SearchFilter);         
        int start = lead.getPage() * lead.getSize();
        int end = Math.min(start + lead.getSize(), mappedPlayers.size());
        return mappedPlayers.subList(start, end); //Ritorna solo la porzione di lista richiesta
    }




    public String updateLeaderboard(String mail, int additionalScore, boolean isWinner) {
        try {
            Player entry = leaderboardDatabaseService.getPlayerByMail(mail);

            if (entry == null) {
                entry = new Player(mail, 0, 0, 0);
            }

            entry.setMatches(entry.getMatches() + 1);
            if (isWinner) {
                entry.setWins(entry.getWins() + 1);
            }
            entry.setTotalScore(entry.getTotalScore() + additionalScore);

            Player updatedEntry = leaderboardDatabaseService.createOrUpdatePlayer(entry);
            if (updatedEntry != null && updatedEntry.getTotalScore() == entry.getTotalScore()) {
                System.out.println("Leaderboard aggiornata correttamente per " + mail);
                return "Leaderboard aggiornata correttamente per " + mail;
            } else {
                throw new RuntimeException("Errore di validazione dopo l'aggiornamento della leaderboard per " + mail);
            }
        } catch (Exception e) {
            System.out.println("Errore durante l'aggiornamento della leaderboard: " + e.getMessage());
            throw new RuntimeException("Errore durante l'aggiornamento della leaderboard per " + mail, e);
        }
    }
    
    public List<Player> getLeaderboard(){
        return leaderboardDatabaseService.getAllPlayers();
    }


}
