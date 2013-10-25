package wikiprocessor.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observable;

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
import wikiprocessor.logger.util.Article;

/**
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 2.0
 * @since 2013.09.26.
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
	
	// private parser
	private WikiParser parser = null;
	
	/**
	 * constructs a worker
	 * @param observable observable QueueManager
	 * @param database connector to H2DB
	 * @param wikiObserver 
	 */
	public WikiWorker(Observable observable, DBConnectorService database, WikiObserver wikiObserver) {
		this.observable = observable;
		this.database = database;		

		this.parser = new ParsoidParser(wikiObserver);
//		this.parser = new SimpleParser();
//		this.parser = new DumbRegexWikiParser();
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
			Article article = queuemanager.pollFromQueue();
			Document doc = downloadArticle(article);

			// don not extract article's text if the article was not downloaded 
			String wikiText = null;
			if (doc != null) {
				wikiText = getRawWikitext(doc);
				
				// if wikiText has been found
				if (wikiText != null) {
					// if newer version is in the DB then do nothing
					if (!database.searchNewer(article.getTitle(), article.getRevision())) {
						// parsing wikiText
						String parsedText = parser.parse(wikiText);
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
	}
	
	/**
	 * downloads wikipedia article's text
	 * @return article in XML format
	 */
	public Document downloadArticle(Article article) {
		// downloading xml through WikipediaAPI
		String queryurl = "http://en.wikipedia.org/w/api.php?action=query&prop=revisions&rvprop=content&format=xml&titles=";
		queryurl += article.getTitle().replace(" ", "_");
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
		return doc;
	}
	
	/**
	 * get raw wikitext content from xml with xpath
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
