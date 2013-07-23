package wikiprocessor.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import wikiprocessor.dbconnector.service.DBConnectorService;
import wikiprocessor.logger.util.Article;

/**
 * 
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.07.23.
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
				ParserActivator.logger.error("Could not find QueueManager instance in OSGi pool!");
				e.printStackTrace();
			}
		}
		
		// downloading xml through WikipediaAPI
		String queryurl = "http://en.wikipedia.org/w/api.php?action=query&prop=revisions&rvprop=content&format=xml&titles=";
		Article article = queuemanager.pollFromQueue();
		String articleTitle = article.getTitle();
		queryurl += articleTitle.replace(" ", "_");
		Document doc = null;
		try {
			URL url = new URL(queryurl);
			InputStream stream = url.openStream();
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
		} catch (MalformedURLException e) {
			ParserActivator.logger.warn("Can not download XML file!");
			e.printStackTrace();
		} catch (SAXException e) {
			ParserActivator.logger.warn("Can not parse downloaded XML!");
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			ParserActivator.logger.warn("Error by configuring parser!");
			e.printStackTrace();
		} catch (IOException e) {
			ParserActivator.logger.warn("Error in connection while downloading XML!");
			e.printStackTrace();
		}

		// don not extract article's text if the article was not downloaded 
		String wikiText = null;
		if (doc != null) {
			wikiText = getRawWikitext(doc);
		}
		
		// if wikiText has been found
		if (wikiText != null) {
	        // parsing wikiText
//				WikiParser parser = new SimpleParser();
			WikiParser parser = new DumbRegexWikiParser();
			article.setText(parser.parse(wikiText));
			
			// if newer version is in the DB then do nothing
			if (!database.searchNewer(article.getTitle(), article.getRevision())) {
				// check if in DB has older version
				if (!database.searchOlder(article.getTitle(), article.getRevision())) {
					// if not: insert
					database.insert(article);
					ParserActivator.statistics.increaseInsertedArticlesCount();
				} else {
					// else: update
					database.update(article);
					ParserActivator.statistics.increaseUpdatedArticlesCount();
				}
			} else {
				ParserActivator.statistics.increaseNotProcessedArticlesCount();
			}
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
				ParserActivator.logger.warn("Could not find wikitext content in xml!");
				return null;
			}
		} catch (XPathExpressionException e) {
			ParserActivator.logger.warn("Could not find wikitext content!");
			e.printStackTrace();
		}
		return wikitextContent;
	}
	
	public static String docToString(Document doc) {
	    try {
	        StringWriter sw = new StringWriter();
	        TransformerFactory tf = TransformerFactory.newInstance();
	        Transformer transformer = tf.newTransformer();
	        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
	        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

	        transformer.transform(new DOMSource(doc), new StreamResult(sw));
	        return sw.toString();
	    } catch (Exception ex) {
	        throw new RuntimeException("Error converting to String", ex);
	    }
	}


}
