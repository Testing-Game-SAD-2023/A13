// added by GaetanoM
package com.g2.model.dto;

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
}
