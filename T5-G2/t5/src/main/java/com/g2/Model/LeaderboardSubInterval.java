package com.g2.Model;

import java.util.Arrays;

public class LeaderboardSubInterval {
    private PlayerStats[] positions;
    private Long totalLength;

    public PlayerStats[] getPositions() {
        return positions;
    }

    public void setPositions(PlayerStats[] positions) {
        this.positions = positions;
    }

    public Long getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(Long totalLength) {
        this.totalLength = totalLength;
    }

    @Override
    public String toString() {
        return "LeaderboardSubInterval{" +
                "positions=" + Arrays.toString(positions) +
                ", totalLength=" + totalLength +
                '}';
    }

}
