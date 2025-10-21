package com.g2.game.gameMode.Compile;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.springframework.stereotype.Service;

@Service
public class CoverageService {
    // Funzione migliorata per ottenere la copertura
    public CoverageResult getCoverage(String xmlContent, String coverageType) {
        Document doc = Jsoup.parse(xmlContent, "", Parser.xmlParser());
        Element counter = doc.selectFirst("report > counter[type=" + coverageType + "]");
        if (counter == null) {
            String errorMessage = "Elemento 'counter' di tipo '" + coverageType + "' non trovato.";
            return new CoverageResult(errorMessage);
        }
        int covered = Integer.parseInt(counter.attr("covered"));
        int missed = Integer.parseInt(counter.attr("missed"));
        return new CoverageResult(covered, missed);
    }

}
