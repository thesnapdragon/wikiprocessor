package wikiprocessor.parser;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import wikiprocessor.dbconnector.service.DBConnectorService;

/**
 * 
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 2.0
 * @since 2013.09.26.
 * 
 * observes QueueManager and handles Wikiworker
 */
public class WikiObserver implements Observer {
	
	// DB bundle's service
	private DBConnectorService database;
	
	private ExecutorService executor = null;
	
	private static int parsoidId = 1;
	
	/**
	 * stores DB bundle's interface
	 * @param db DB bundle's service instance
	 */
	public WikiObserver(DBConnectorService db) {
		this.database = db;
		executor = Executors.newFixedThreadPool(19);
	}
	
	/**
	 * handles changes of the QueueManager
	 * @param observable QueueManager instance
	 * @param arg1 other arguments (not used)
	 */
	@Override
	public void update(Observable observable, Object arg1) {
		executor.execute(new Thread(new WikiWorker(observable, database)));
	}
	
	public static synchronized void increaseParsoidId() {
		parsoidId++;
	}
	
	public static synchronized void decreaseParsoidId() {
		parsoidId--;
	}
	
	public static synchronized int getParsoidId() {
		return parsoidId;
	}

}
