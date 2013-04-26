package wikiprocessor.parser;

import hu.sztaki.sztakipediaparser.wiki.converter.IWikiInterpreter;
import hu.sztaki.sztakipediaparser.wiki.converter.PlainWikiInterpreter;
import hu.sztaki.sztakipediaparser.wiki.parser.Parser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * 
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.04.26.
 *
 * QueueManager implementation
 * 
 */
public class SimpleParser extends Parser {

	public SimpleParser(IWikiInterpreter interpreter) {
		super(interpreter);
	}
	
	public SimpleParser() throws MalformedURLException, NoSuchAlgorithmException, IOException {
		this(new PlainWikiInterpreter(new Locale("en")));
	}

	public String parse(String wikiText) {
		String parsedText = super.parse(wikiText);
		return parsedText;
	}

}
