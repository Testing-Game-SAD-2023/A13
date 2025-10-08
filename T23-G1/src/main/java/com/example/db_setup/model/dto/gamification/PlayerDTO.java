// added by GaetanoM
package com.example.db_setup.model.dto.gamification;

import com.example.db_setup.model.Player;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDTO {
	private long id;
	private String name;
	private String surname;
	private String nickname;
	private String email;
	private String studies;
	private PlayerProgressDTO playerProgress;

	public PlayerDTO(Player player) {
		this(
			player.getID(),
			player.getName(),
			player.getSurname(),
			player.getNickname(),
			player.getEmail(),
			player.getStudies().toString(),
			new PlayerProgressDTO(player.getPlayerProgress())
		);
	}
}
