package randoop;

import java.io.File;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

public class EmmaCoverage {
	public static int LineCoverage(String path) {
		Element line = null;
		String linecoverage= null;
		try {
			
			File cov = new File(path);
			
			Document doc = Jsoup.parse(cov, null, "", Parser.xmlParser());
			line = doc.getElementsByTag("coverage").get(3);
			linecoverage = String.valueOf(line).substring(32, 34);
			
			if(linecoverage.contains("%"))
				linecoverage = linecoverage.substring(0, 1);
	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Integer.valueOf(linecoverage) ;
	}

}
