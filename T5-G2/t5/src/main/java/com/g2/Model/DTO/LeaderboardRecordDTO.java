// added by GaetanoM
// Classe DTO che rappresenta la singola riga della classifica
package com.g2.Model.DTO;

import com.g2.Model.LeaderboardRecord;
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
public class LeaderboardRecordDTO {
	private String name;
	private String surname;
	private String email;
	private int exp;
	private int wins;

	public LeaderboardRecordDTO(LeaderboardRecord leaderboardRecord) {
		this.name = leaderboardRecord.getName();
		this.surname = leaderboardRecord.getSurname();
		this.email = leaderboardRecord.getEmail();
		this.exp = leaderboardRecord.getExp();
		this.wins = leaderboardRecord.getWins();
	}
}
