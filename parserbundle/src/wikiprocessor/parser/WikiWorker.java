package wikiprocessor.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observable;

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
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.08.12.
 * 
 * Worker class to do time consuming jobs
 */
public class WikiWorker implements Runnable {

	// observable QueueManager
	private Observable observable = null;
	// connector to H2DB
	private DBConnectorService database = null;
	// QueueManager instance
	private QueueManager queuemanager = null;
	
	/**
	 * constructs a worker
	 * @param observable observable QueueManager
	 * @param database connector to H2DB
	 */
	public WikiWorker(Observable observable, DBConnectorService database) {
		this.observable = observable;
		this.database = database;
	}
	
	/**
	 * runs the thread: start working
	 */
	@Override
	public void run() {
		if (observable instanceof QueueManager) {
			queuemanager = (QueueManager) observable;
		} else {
			try {
				throw new WrongObserverException();
			} catch (WrongObserverException e) {
				ParserActivator.logger.error("Could not find QueueManager instance in OSGi pool!");
				ParserActivator.statistics.increaseDebugLogCount();
				e.printStackTrace();
			}
		}
		
		work();
	}
	
	/**
	 * do the jobs: downloads articles, parses articles, handles DB
	 */
	public void work() {
		while (queuemanager.getSize() > 0) {
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
				ParserActivator.statistics.increaseWarningLogCount();
				e.printStackTrace();
			} catch (SAXException e) {
				ParserActivator.logger.warn("Can not parse downloaded XML!");
				ParserActivator.statistics.increaseWarningLogCount();
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				ParserActivator.logger.warn("Error by configuring parser!");
				ParserActivator.statistics.increaseWarningLogCount();
				e.printStackTrace();
			} catch (IOException e) {
				ParserActivator.logger.warn("Error in connection while downloading XML!");
				ParserActivator.statistics.increaseWarningLogCount();
				e.printStackTrace();
			}

			// don not extract article's text if the article was not downloaded 
			String wikiText = null;
			if (doc != null) {
				wikiText = getRawWikitext(doc);
			}
			
			// if wikiText has been found
			if (wikiText != null) {
				
				// if newer version is in the DB then do nothing
				if (!database.searchNewer(article.getTitle(), article.getRevision())) {
					
			        // parsing wikiText
//						WikiParser parser = new SimpleParser();
					WikiParser parser = new DumbRegexWikiParser();
					
					String parsedText = parser.parse(wikiText);
					
					// if parsed text is not empty
					if (parsedText != null && !parsedText.isEmpty()) {
						article.setText(parsedText);
						
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
					}
				} else {
					ParserActivator.statistics.increaseNotProcessedArticlesCount();
				}
			}
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
				ParserActivator.statistics.increaseWarningLogCount();
				
				return null;
			}
		} catch (XPathExpressionException e) {
			ParserActivator.logger.warn("Could not find wikitext content!");
			ParserActivator.statistics.increaseWarningLogCount();
			e.printStackTrace();
		}
		return wikitextContent;
	}
	
	/**
	 * utility method for convert xml to string; helps debugging [NOT USED]
	 * @param doc xml to convert
	 * @return string form of the xml
	 */
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
	
	/**
	 * own class for adding wrong Observer to QueueManager
	 */
	@SuppressWarnings("serial")
	public class WrongObserverException extends Exception {
		public WrongObserverException() {}

		public WrongObserverException(String message) {
			super(message);
		}
	}

}