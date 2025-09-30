// added by GaetanoM
// TODO: remove this service
package com.g2.Service;

import com.g2.Model.DTO.LeaderboardRecordDTO;
import com.g2.Model.DTO.PlayerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LeaderboardService {
	private static final Logger logger = LoggerFactory.getLogger(LeaderboardService.class);

	public List<LeaderboardRecordDTO> getLeaderboard(List<PlayerDTO> players) {
		return players.stream().map(LeaderboardRecordDTO::new).sorted().toList();
	}
}
