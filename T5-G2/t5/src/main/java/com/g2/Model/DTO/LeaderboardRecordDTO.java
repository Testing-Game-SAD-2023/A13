// added by GaetanoM
package com.g2.Model.DTO;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class LeaderboardRecordDTO implements Comparable<LeaderboardRecordDTO> {
	private String name;
	private String surname;
	private String email;
	private int score;

	public LeaderboardRecordDTO(PlayerDTO player, int score) {
		this.name = player.getName();
		this.surname = player.getSurname();
		this.email = player.getEmail();
		this.score = score;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		LeaderboardRecordDTO that = (LeaderboardRecordDTO) o;
		return getScore() == that.getScore() && Objects.equals(getName(), that.getName()) && Objects.equals(getSurname(), that.getSurname()) && Objects.equals(getEmail(), that.getEmail());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getName(), getSurname(), getEmail(), getScore());
	}

	@Override
	public int compareTo(LeaderboardRecordDTO otherRecord) {
		return Integer.compare(otherRecord.score, this.score); // Ordina in ordine decrescente
	}
}
