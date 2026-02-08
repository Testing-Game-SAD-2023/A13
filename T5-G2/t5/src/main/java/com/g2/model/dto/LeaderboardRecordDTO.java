// added by GaetanoM
// Classe DTO che rappresenta la singola riga della classifica
package com.g2.model.dto;

import com.g2.model.LeaderboardRecord;
import lombok.*;

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
