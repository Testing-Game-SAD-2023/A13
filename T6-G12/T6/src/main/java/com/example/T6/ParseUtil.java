package com.example.T6;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

public class ParseUtil {
    public static int LineCoverage(String cov) {
		Element line = null;

        Document doc = Jsoup.parse(cov, "", Parser.xmlParser());
        
        line = doc.selectFirst("report > counter[type=LINE]");
		
		return 100 * Integer.parseInt(line.attr("covered")) / (Integer.parseInt(line.attr("covered")) + Integer.parseInt(line.attr("missed")));
	}
}
