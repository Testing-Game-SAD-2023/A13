// added by GaetanoM
package com.g2.Model.DTO;

import com.g2.Model.OpponentSummary;
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
public class LeaderboardRecordDTO implements Comparable<LeaderboardRecordDTO> {
	private String name;
	private String surname;
	private String email;
	private int exp;
	private int wins;

	public LeaderboardRecordDTO(PlayerDTO player) {
		this.name = player.getName();
		this.surname = player.getSurname();
		this.email = player.getEmail();
		this.exp = getPlayerExp(player);
		this.wins = getPlayerWins(player);
	}

	private int getPlayerExp(PlayerDTO player){
		return player.getPlayerProgress().getExperiencePoints();
	}

	private int getPlayerWins(PlayerDTO player){
		List<GameProgressDTO> gameProgresses = player.getPlayerProgress().getGameProgressesDTO();
//		logger.info("Player {} - Game Progresses: {}", player.getId(), gameProgresses);
		Set<OpponentSummary> gameProgressSet = gameProgresses.stream()
			.filter(gameProgress -> gameProgress.getGameMode() == GameMode.PartitaSingola && gameProgress.isWon())
			.map(gameProgress -> new OpponentSummary(gameProgress.getClassUT(), gameProgress.getType(), gameProgress.getDifficulty()))
			.collect(Collectors.toSet());
//		logger.info("Player {} - Game Progresses Set: {}", player.getId(), gameProgressSet);
		return gameProgressSet.size();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		LeaderboardRecordDTO that = (LeaderboardRecordDTO) o;
		return getWins() == that.getWins() && getExp() == that.getExp() && Objects.equals(getName(), that.getName()) && Objects.equals(getSurname(), that.getSurname()) && Objects.equals(getEmail(), that.getEmail());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getName(), getSurname(), getEmail(), getExp(), getWins());
	}

	@Override
	public int compareTo(LeaderboardRecordDTO otherRecord) {
		return Integer.compare(otherRecord.getExp(), this.getExp()); // Ordina in ordine decrescente
	}
}
