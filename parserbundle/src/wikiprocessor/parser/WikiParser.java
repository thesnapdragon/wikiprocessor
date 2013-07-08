package wikiprocessor.parser;

/**
 * 
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.07.08.
 * 
 * Interface for parsing WikiText
 */
public interface WikiParser {
	
	/**
	 * parsing WikiText
	 * 
	 * @param wikiText to parse
	 * @return parsed text
	 */
	public String parse(String wikiText);
	
}
