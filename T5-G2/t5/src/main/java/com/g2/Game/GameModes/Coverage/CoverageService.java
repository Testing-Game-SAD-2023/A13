package com.g2.Game.GameModes.Coverage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CoverageService {

    // Logger per la classe
    private static final Logger logger = LoggerFactory.getLogger(CoverageService.class);

    // Funzione migliorata per ottenere la copertura
    public CoverageResult getCoverage(String xmlContent, String coverageType) {
            Document doc = Jsoup.parse(xmlContent, "", Parser.xmlParser());
            Element counter = doc.selectFirst("report > counter[type=" + coverageType + "]");
            if (counter == null) {
                String errorMessage = "Elemento 'counter' di tipo '" + coverageType + "' non trovato.";
                logger.error(errorMessage);
                return new CoverageResult(errorMessage);
            }
            int covered = Integer.parseInt(counter.attr("covered"));
            int missed = Integer.parseInt(counter.attr("missed"));
            logger.info("{} Coverage - Covered: {}, Missed: {}", coverageType, covered, missed);
            return new CoverageResult(covered, missed);
    }

}
