package wikiprocessor.parser;

import java.util.Observable;
import java.util.Observer;

import wikiprocessor.dbconnector.service.DBConnectorService;

/**
 * 
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.08.12.
 * 
 * observes QueueManager and handles Wikiworker
 */
public class WikiObserver implements Observer {
	
	// DB bundle's service
	private DBConnectorService database;
	
	// WikiWorker instance (downloads, parses, handles DB)
	private Thread wikiWorker = null;
	
	/**
	 * stores DB bundle's interface
	 * @param db DB bundle's service instance
	 */
	public WikiObserver(DBConnectorService db) {
		this.database = db;
	}
	
	/**
	 * handles changes of the QueueManager
	 * @param observable QueueManager instance
	 * @param arg1 other arguments (not used)
	 */
	@Override
	public void update(Observable observable, Object arg1) {
		if (wikiWorker != null && wikiWorker.getState() == Thread.State.TERMINATED) {
			wikiWorker = null;
		}
		
		if (wikiWorker == null) {
			wikiWorker = new Thread(new WikiWorker(observable, database));
		}
		
		if (wikiWorker != null && !wikiWorker.isAlive()) {
			wikiWorker.start();
		}
	}

}
