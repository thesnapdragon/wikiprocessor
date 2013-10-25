package wikiprocessor.dump;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import wikiprocessor.parser.ParserActivator;

/**
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.10.24.
 * 
 * Preprocessing Wikipedia dump: counts pages
 */
public class DumpPreprocessor extends DefaultHandler {

	// count of Wikipedia pages
	private int pages = 0;
 
	/**
	 * when page element found increment pagecount
	 */
	public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("PAGE")) {
			pages++;
			if (pages % 100000 == 0) {
				ParserActivator.logger.trace("Dump preprocessing status: " + String.valueOf(pages) + " pages were counted");
			}
		}
	}

	/**
	 * getters of pagecount
	 * @return
	 */
	public int getPages() {
		return pages;
	}
}
