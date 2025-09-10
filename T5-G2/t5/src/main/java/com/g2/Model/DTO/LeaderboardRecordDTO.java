// added by GaetanoM
package com.g2.Model.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class LeaderboardRecordDTO implements Comparable<LeaderboardRecordDTO> {
	private Long id;
	private String name;
	private String surname;
	private String email;
	private int score;

	public LeaderboardRecordDTO(PlayerDTO player) {
		this.id = player.getId();
		this.name = player.getName();
		this.surname = player.getSurname();
		this.email = player.getEmail();
		this.score = player.getPlayerProgress().getExperiencePoints();
	}

	@Override
	public int compareTo(LeaderboardRecordDTO otherRecord) {
		return Integer.compare(otherRecord.score, this.score); // Ordina in ordine decrescente
	}
}
