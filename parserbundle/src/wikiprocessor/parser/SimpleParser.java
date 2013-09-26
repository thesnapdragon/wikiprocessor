package wikiprocessor.parser;

import hu.sztaki.sztakipediaparser.wiki.converter.PlainWikiInterpreter;
import hu.sztaki.sztakipediaparser.wiki.parser.Parser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.07.08.
 *
 * QueueManager implementation
 */
public class SimpleParser implements WikiParser {
	
	// own parser instance
	private Parser parser = null;
	
	/**
	 * adapter constructor: creates a new parser, that can parse wikiText to plain text
	 */
	public SimpleParser() {
		try {
			this.parser = new Parser(new PlainWikiInterpreter(new Locale("en")));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * parsing wikiText to plain text
	 * @param wikiText wikiText to parse
	 * @return parsedText
	 */
	@Override
	public String parse(String wikiText) {
		String parsedText = this.parser.parse(wikiText);
		return parsedText;
	}

}
