package wikiprocessor.dump;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import wikiprocessor.logger.util.Article;
import wikiprocessor.parser.ParserActivator;
import wikiprocessor.parser.QueueManager;

/**
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.10.25.
 * 
 * Read the whole Wikipedia dump into Article objects
 * TODO: comments
 */
public class DumpReader extends DefaultHandler {

    private Article article = null;
    private QueueManager dumpQueueManager = null;

	private StringBuilder currentText = null;
    private boolean revision = false;
	private boolean title = false;
	private boolean id = false;
	private boolean text = false;
 
	public DumpReader(QueueManager dumpQueueManager) {
		this.dumpQueueManager = dumpQueueManager;
	}

	public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("TITLE")) {
			title = true;
			article = new Article();
		}
		if (qName.equalsIgnoreCase("REVISION")) {
			revision = true;
		}
		if (revision && qName.equalsIgnoreCase("ID")) {
			id = true;
		}
		if (qName.equalsIgnoreCase("TEXT")) {
			text = true;
			currentText = new StringBuilder();
		}
	}
 
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase("TEXT")) {
			text = false;
		}
		if (qName.equalsIgnoreCase("PAGE")) {
			article.setText(currentText.toString());
			currentText = null;
			dumpQueueManager.addToQueue(article);
			ParserActivator.logger.trace("Article added to dump Queue: " + article.getTitle());
			ParserActivator.statistics.increaseProcessedPages();
		}
	}
 
	public void characters(char ch[], int start, int length) throws SAXException {
		if (title) {
			article.setTitle(new String(ch, start, length));
			title = false;
		}
		if (id) {
			article.setRevision(Integer.parseInt(new String(ch, start, length)));
			id = false;
			revision = false;
		}
		if (text) {
			currentText.append(new String(ch, start, length));
		}
	}

}
