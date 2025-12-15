package com.example.t9profileservice.llm;

import com.example.t9profileservice.dto.PlayerBioData;

import java.time.format.DateTimeFormatter;

public class MockLLMClient implements LlmClient {

    @Override
    public String generateBio(PlayerBioData data) {
        if (data == null) return "Giocatore anonimo, pronto a nuove sfide!";

        String name = data.name() != null ? data.name() : "";
        String nick = data.nickname() != null ? data.nickname() : "";

        StringBuilder sb = new StringBuilder();
        sb.append("Profilo giocatore: ");

        if (!name.isEmpty() || !nick.isEmpty()) {
            if (!name.isEmpty() && !nick.isEmpty()) sb.append(name).append(" (").append(nick).append(")");
            else if (!name.isEmpty()) sb.append(name);
            else sb.append(nick);
            sb.append(" — ");
        }

        Integer matches = data.matchesPlayed();
        Integer wins = data.matchesWon();
        if (matches != null && matches > 0) {
            sb.append("ha giocato ").append(matches).append(" partita").append(matches == 1 ? "" : "e");
            if (wins != null) {
                sb.append(", con ").append(wins).append(" vittoria").append(wins == 1 ? "" : "e");
            }
            sb.append(". ");
        }

        if (data.lastMatchAt() != null) {
            try {
                String when = data.lastMatchAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                sb.append("Attivo di recente (ultima partita: ").append(when).append("). ");
            } catch (Exception ignored) {
                sb.append("Attivo di recente. ");
            }
        }

        if ((matches == null || matches == 0) && data.lastMatchAt() == null) {
            sb.append("Giocatore curioso e amichevole, sempre pronto a migliorare le proprie abilità.");
        } else {
            sb.append("Competitivo ma divertente — pronto per nuove sfide!");
        }

        // Ensure approx length <= 250 chars
        String bio = sb.toString().trim();
        if (bio.length() > 250) bio = bio.substring(0, 247) + "...";
        return bio;
    }
}
