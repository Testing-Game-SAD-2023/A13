// added by GaetanoM
package com.g2.Components;

import com.g2.Interfaces.ServiceManager;
import com.g2.Model.DTO.LeaderboardRecordDTO;
import com.g2.Model.DTO.PlayerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

// LogicComponent che esegue la logica e la chiamata API per la costruzione della classifica
public class LeaderboardComponent extends GenericLogicComponent {
	private final String ErrorCode = "leaderboard_error";
	private final ServiceManager serviceManager;
	private final Long currentPlayerId;
	private final GenericObjectComponent leaderboardObjectComponent;
	private static final Logger logger = LoggerFactory.getLogger(LeaderboardComponent.class);

	public LeaderboardComponent(
		GenericObjectComponent leaderboardObjectComponent,
		Long currentPlayerId,
		ServiceManager serviceManager
	) {
		this.serviceManager = serviceManager;
		this.currentPlayerId = currentPlayerId;
		this.leaderboardObjectComponent = leaderboardObjectComponent;
	}

	@Override
	public boolean executeLogic() {
		List<PlayerDTO> players = (List<PlayerDTO>) serviceManager.handleRequest("T23", "getAllPlayers", (Object) null);
		logger.debug("Players retrieved: {}", players);
		Optional<PlayerDTO> optCurrentPlayer = players.stream().filter(player -> player.getId() == this.currentPlayerId).findFirst();
		if(optCurrentPlayer.isEmpty()) return false;
		PlayerDTO currentPlayer = optCurrentPlayer.get();
		List<LeaderboardRecordDTO> leaderboard = players.stream().map(LeaderboardRecordDTO::new).sorted().toList();
		logger.debug("Leaderboard: {}", leaderboard);
		LeaderboardRecordDTO currentPlayerRecord = new LeaderboardRecordDTO(currentPlayer);
		logger.debug("Current player record: {}", currentPlayerRecord);
//		la leaderboard è inserita all'interno di un ObjectComponent che sarà passato al PageBuilder per la
//		costruzione della pagina della classifica
		leaderboardObjectComponent.setObject("leaderboard", leaderboard);
		leaderboardObjectComponent.setObject("playerRecord", currentPlayerRecord);
		return true;
	}

	@Override
	public String getErrorCode() {
		return this.ErrorCode;
	}
}
