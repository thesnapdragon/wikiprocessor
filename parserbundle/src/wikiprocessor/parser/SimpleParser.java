package wikiprocessor.parser;

import hu.sztaki.sztakipediaparser.wiki.converter.IWikiInterpreter;
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
public class SimpleParser extends Parser implements WikiParser {

	/**
	 * original constructor (wrapped)
	 * @param interpreter wikiText interpreter
	 */
	public SimpleParser(IWikiInterpreter interpreter) {
		super(interpreter);
	}
	
	/**
	 * wrapper constructor: creates a new parser, that can parse wikiText to plain text
	 * @throws MalformedURLException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public SimpleParser() throws MalformedURLException, NoSuchAlgorithmException, IOException {
		this(new PlainWikiInterpreter(new Locale("en")));
	}

	/**
	 * parsing wikiText to plain text
	 * @param wikiText wikiText to parse
	 * @return parsedText
	 */
	public String parse(String wikiText) {
		String parsedText = super.parse(wikiText);
		return parsedText;
	}

}
