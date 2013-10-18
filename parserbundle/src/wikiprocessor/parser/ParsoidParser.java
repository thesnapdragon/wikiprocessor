package wikiprocessor.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.10.18.
 * 
 * Parser class using the original MediaWiki Parsoid parser
 */
public class ParsoidParser implements WikiParser {

	// WikiObserver instance
	private WikiObserver wikiObserver = null;
	// url to connect
	private String urlString = null;
	
	public ParsoidParser(WikiObserver wikiObserver) {
		this.wikiObserver = wikiObserver;
	}

	/**
	 * parsing wikiText to plain text
	 * @param wikiText wikiText to parse
	 * @return parsedText
	 */
	@Override
	public String parse(String wikiText) {
		urlString = wikiObserver.getParsoidInstance();
		String parsedText = null;
		try {
			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();

			conn.setDoOutput(true);

			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

			writer.write(wikiText);
			writer.flush();
	
			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	
			while ((line = reader.readLine()) != null) {
			    parsedText = parsedText + line;
			}
			writer.close();
			reader.close();
		
		} catch (MalformedURLException e) {
			ParserActivator.logger.error("Error while connecting to Parsoid " + urlString + "!");
			ParserActivator.statistics.increaseErrorLogCount();
		} catch (IOException e) {
			ParserActivator.logger.error("Error in connection with Parsoid " + urlString + "!");
			ParserActivator.statistics.increaseErrorLogCount();
		}
		wikiObserver.putParsoidInstance(urlString);
		return parsedText;
	}

}
