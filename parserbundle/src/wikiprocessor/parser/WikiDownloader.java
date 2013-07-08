package wikiprocessor.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import wikiprocessor.dbconnector.service.DBConnectorService;

/**
 * 
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.07.08.
 * 
 * downloads Wikipedia articles
 */
public class WikiDownloader implements Observer {
	
	// DB bundle's service
	private DBConnectorService database;
	
	/**
	 * stores DB bundle's interface
	 * @param db DB bundle's service instance
	 */
	public WikiDownloader(DBConnectorService db) {
		this.database = db;
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		// get QueueManager
		QueueManager queuemanager = null;
		if (arg0 instanceof QueueManager) {
			queuemanager = (QueueManager) arg0;
		} else {
			try {
				throw new WrongObserverException();
			} catch (WrongObserverException e) {
				System.out.println("ERROR! Could not find QueueManager instance in OSGi pool!");
				e.printStackTrace();
			}
		}
		
		// downloading xml through WikipediaAPI
		String queryurl = "http://en.wikipedia.org/w/api.php?action=query&prop=revisions&rvprop=content&format=xml&titles=";
		queryurl += queuemanager.pollFromQueue().replace(" ", "_");
		Document doc = null;
		try {
			URL url = new URL(queryurl);
			InputStream stream = url.openStream();
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
		} catch (MalformedURLException e) {
			System.err.println("ERROR! Can not download XML file!");
			e.printStackTrace();
		} catch (SAXException e) {
			System.err.println("ERROR! Can not parse downloaded XML!");
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			System.err.println("ERROR! Can not parse downloaded XML!");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("ERROR! Error while downloading XML!");
			e.printStackTrace();
		}

		String wikiText = getRawWikitext(doc);
		// if wikiText has been found
		if (wikiText != null) {
	        // parsing wikiText
//				WikiParser parser = new SimpleParser();
			WikiParser parser = new DumbRegexWikiParser();
			String parsedText = parser.parse(wikiText);
			// adding parsedText to database bundle
			database.insert(parsedText);
		}
	}

	/**
	 * own class for adding wrong Observer to QueueManager
	 *
	 */
	@SuppressWarnings("serial")
	public class WrongObserverException extends Exception {
		public WrongObserverException() {}

		public WrongObserverException(String message) {
			super(message);
		}
	}
	
	/**
	 * get raw wikitext content from xml with xpath
	 * 
	 * @param doc xml to parse
	 * @return raw wikitext from xml
	 */
	public String getRawWikitext(Document doc) {
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		
		String wikitextContent = null;		
		try {
			// get node where the text is
			Node wikitext = (Node) xpath.evaluate("//rev", doc, XPathConstants.NODE);
			if (wikitext != null) {
				wikitextContent = wikitext.getTextContent();
			} else {
				System.err.println("ERROR! Could not find wikitext content in xml!");
				return null;
			}
		} catch (XPathExpressionException e) {
			System.err.println("ERROR! Could not find wikitext content!");
			e.printStackTrace();
		}
		return wikitextContent;
	}
}
