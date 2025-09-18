// added by GaetanoM
package com.g2.Service;

import com.g2.Model.DTO.GameProgressDTO;
import com.g2.Model.DTO.LeaderboardRecordDTO;
import com.g2.Model.DTO.PlayerDTO;
import com.g2.Model.OpponentSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import testrobotchallenge.commons.models.opponent.GameMode;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LeaderboardService {
	private static final Logger logger = LoggerFactory.getLogger(LeaderboardService.class);

	public List<LeaderboardRecordDTO> getLeaderboard(List<PlayerDTO> players) {
		return players.stream().map(player -> new LeaderboardRecordDTO(player, this.getPlayerScore(player))).sorted().toList();
	}

	public int getPlayerScore(PlayerDTO player){
		List<GameProgressDTO> gameProgresses = player.getPlayerProgress().getGameProgressesDTO();
		logger.info("Player {} - Game Progresses: {}", player.getId(), gameProgresses);
		Set<OpponentSummary> gameProgressSet = gameProgresses.stream()
			.filter(gameProgress -> gameProgress.getGameMode() == GameMode.PartitaSingola && gameProgress.isWon())
			.map(gameProgress -> new OpponentSummary(gameProgress.getClassUT(), gameProgress.getType(), gameProgress.getDifficulty()))
			.collect(Collectors.toSet());
		logger.info("Player {} - Game Progresses Set: {}", player.getId(), gameProgressSet);
		return gameProgressSet.size();
	}
}
