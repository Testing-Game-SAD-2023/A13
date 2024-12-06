package com.g2.Model;

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
 
}
