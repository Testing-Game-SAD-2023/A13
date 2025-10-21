package com.g2.model.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameExecutionConfig {
    @JsonProperty("compute_evosuite_metrics_only_at_end_game")
    private boolean executeEvosuiteOnlyAtEndGame;
}
