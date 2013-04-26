package wikiprocessor.parser;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

import org.w3c.dom.Document;

/**
 * 
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.04.26.
 * 
 * downloads Wikipedia articles
 */
public class WikiDownloader implements Observer {

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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// downloading xml through WikipediaAPI
		// TODO: continue
		String queryurl = "http://en.wikipedia.org/w/api.php?action=query&prop=revisions&rvprop=content&format=xml&titles=";
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
}
