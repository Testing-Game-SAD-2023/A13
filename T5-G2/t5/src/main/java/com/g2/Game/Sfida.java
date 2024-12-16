/*
 *   Copyright (c) 2024 Stefano Marano https://github.com/StefanoMarano80017
 *   All rights reserved.

 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at

 *   http://www.apache.org/licenses/LICENSE-2.0

 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.g2.Game;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.g2.Interfaces.ServiceManager;

public class Sfida extends GameLogic {

    private int currentTurn;
    private int userScore;
    private int robotScore;
    private int totalTurns = 10;
    private Boolean GameOVer = false;
    private double lines_factor=1;


    //Questa classe si specializza in una partita semplice basata sui turni, prende il nome di Sfida nella UI
    public Sfida(ServiceManager serviceManager, String PlayerID, String ClasseUT,
                                String type_robot, String difficulty, String gamemode) {
        super(serviceManager, PlayerID, ClasseUT, type_robot, difficulty, gamemode);
        currentTurn = 0;
    }

    @Override
    public void playTurn(double userScore, int robotScore) {
        String Time = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
        currentTurn++;
        //CreateTurn(Time, userScore);
        System.out.println("[GAME] Turn " + currentTurn + " played. User Score: " + userScore + ", Robot Score: " + robotScore);
    }

    @Override
    public Boolean isGameEnd() {
        return false; //il giocatore può fare quanti turni vuole quindi ritorno sempre false
    }

/*MODIFICA */
        
    @Override
    public int GetScore(String underTestClassCode, int coverage, int numPrivateMethods, int privateMethodsCovered, String difficulty) {
        // Se loc è 0, il punteggio è sempre 0
        if (coverage == 0) {
            return 0;
        }
        // Penalità crescente per ogni turno aggiuntivo
        double penaltyFactor = Math.pow(0.9, currentTurn);
       
      
        /*MODIFICA aggingo un moltiplicatore se le linee di codice coperte superano un certo valore */
        
        int num_lines=Punteggio_util.countCodeLines(underTestClassCode);
        
        /*MODIFICA*/
        /*Se secgli un robot difficile e la copertura al primo turno supera una certa soglia ottieni un moltiplicatore */

        double difficultyBot_factor= Double.parseDouble(difficulty);

        //per dare una maggiore idea di competitività all'utente
        if (currentTurn==0 && coverage>=75){
            if ( difficultyBot_factor==2){
                lines_factor=1.1;
                }
            else if(difficultyBot_factor==3){
                lines_factor=1.3;
            }

        }
       
        double privateMethodsFactor=1;
    
        
    

        //MODIFICA

        //Se ho un numero maggiore di 0 di numeri privati allora aggiungo un fattore moltiplicativo pari al rapoporto tra metodi privati in cui si
        if (numPrivateMethods>0){
         privateMethodsFactor = (double) 1 + privateMethodsCovered/numPrivateMethods;
        }
       
       

        int CyclomaticComplexity=Punteggio_util.calculateCyclomaticComplexity(underTestClassCode);


        /*MODIFICA  considero un fattore statico che tenga conto 
        dellla complessità della classe come complessità ciclomatica, numero di righe totali e numero di metodi private*/
        double difficultyClass=(double) (CyclomaticComplexity+num_lines+numPrivateMethods);

        //do a quest della difficoltà della classe un peso in millessimi
        double difficultyClassFactor=(difficultyClass/1000);

        //considero un valore di difficoltà complessivo come somma della difficoltà del bot e di quella della classe
        double difficulty_factor=difficultyBot_factor+difficultyClassFactor;
        



        /*Qui moltiplico alla percentuale un numero di punti pari alla difficoltà della classe testata e del bot */
        double score_diff=coverage*difficulty_factor;

        /*Qui tengo conto degli eventuali fattori lines_factor e privateMethodsFactor */
        double score_add=(score_diff*lines_factor)*privateMethodsFactor;

        /*Qui tengo conto dell'eventuale fattore di penalizzazione in base al numero di turni */
        double partial_score=score_add*penaltyFactor;
        
            
        int score_finale=Math.toIntExact(Math.round(partial_score));          
            

        

        // Restituisci il punteggio 
        return score_finale;
    }

}
