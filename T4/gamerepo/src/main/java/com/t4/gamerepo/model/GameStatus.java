package com.t4.gamerepo.model;

/**
 * Enumerazione che descrive lo stato nel tempo di una partita, che attualmente può essere:
 * <li>
 *     <ul>CREATED: la partita è stato creata in T4;</ul>
 *     <ul>STARTED: un round è stato creato e associato alla partita;</ul>
 *     <ul>IN_PROGRESS: un turno è stato creato e associato al primo round della partita;</ul>
 *     <ul>FINISHED: la partita è stata terminata dal giocatore e il risultato del game è stato registrato;</ul>
 *     <ul>IN_PROGRESS: il giocatore ha abbandonato la partita senza concluderla;</ul>
 * </li>
 */
public enum GameStatus {
    CREATED,
    STARTED,
    IN_PROGRESS,
    FINISHED,
    SURRENDERED
}
