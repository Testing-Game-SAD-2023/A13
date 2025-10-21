// added by GaetanoM
// Classe DTO che rappresenta la singola riga della classifica
package com.g2.model;

import com.g2.model.dto.GameProgressDTO;
import com.g2.model.dto.PlayerDTO;
import lombok.*;
import testrobotchallenge.commons.models.opponent.GameMode;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class LeaderboardRecord implements Comparable<LeaderboardRecord> {
	private String name;
	private String surname;
	private String email;
	private int exp;
	private int wins;

	public LeaderboardRecord(PlayerDTO player) {
		this.name = player.getName();
		this.surname = player.getSurname();
		this.email = player.getEmail();
		this.exp = getPlayerExp(player);
		this.wins = getPlayerWins(player);
	}

//	Funzione per estrarre i punti esperienza da PlayerDTO
	private int getPlayerExp(PlayerDTO player){
		return player.getPlayerProgress().getExperiencePoints();
	}

//	Funzione per estrarre il numero di partite vinte da un giocatore (senza ripetizioni)
	private int getPlayerWins(PlayerDTO player){
		List<GameProgressDTO> gameProgresses = player.getPlayerProgress().getGameProgressesDTO();
		Set<OpponentSummary> gameProgressSet = gameProgresses.stream()
			.filter(gameProgress -> gameProgress.getGameMode() == GameMode.PartitaSingola && gameProgress.isWon())
			.map(gameProgress -> new OpponentSummary(gameProgress.getClassUT(), gameProgress.getType(), gameProgress.getDifficulty()))
			.collect(Collectors.toSet());
		return gameProgressSet.size();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		LeaderboardRecord that = (LeaderboardRecord) o;
		return getWins() == that.getWins() && getExp() == that.getExp() && Objects.equals(getName(), that.getName()) && Objects.equals(getSurname(), that.getSurname()) && Objects.equals(getEmail(), that.getEmail());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getName(), getSurname(), getEmail(), getExp(), getWins());
	}

	@Override
	public int compareTo(LeaderboardRecord otherRecord) {
		return Integer.compare(otherRecord.getExp(), this.getExp()); // Ordina in ordine decrescente
	}
}
