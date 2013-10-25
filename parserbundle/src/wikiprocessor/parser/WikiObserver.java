package wikiprocessor.parser;

import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import wikiprocessor.dbconnector.service.DBConnectorService;

/**
 * 
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 2.0
 * @since 2013.10.24.
 * 
 * observes QueueManager and handles Wikiworker
 */
public class WikiObserver implements Observer {
	
	// DB bundle's service
	private DBConnectorService database;
	
	private ExecutorService executor = null;
	private LinkedBlockingQueue<String> parsoidList = null;
	
	/**
	 * stores DB bundle's interface
	 * @param db DB bundle's service instance
	 * @param parsoidAddressList 
	 */
	public WikiObserver(DBConnectorService db, String[] parsoidAddressList) {
		parsoidList = new LinkedBlockingQueue<String>(Arrays.asList(parsoidAddressList));
	
		this.database = db;
		executor = Executors.newFixedThreadPool(parsoidList.size());
	}
	
	/**
	 * handles changes of the QueueManager
	 * @param observable QueueManager instance
	 * @param arg1 other arguments (not used)
	 */
	@Override
	public void update(Observable observable, Object arg1) {
		executor.execute(new Thread(new WikiWorker(observable, database, this)));
	}
	
	/**
	 * get a non working Parsoid instance url
	 * @return Parsoid url
	 */
	public synchronized String getParsoidInstance() {
		ParserActivator.statistics.setWorkingParserRatio(parsoidList.size() / (double)parsoidList.size());
		return parsoidList.poll();
	}
	
	public synchronized void putParsoidInstance(String url) {
		ParserActivator.statistics.setWorkingParserRatio(parsoidList.size() / (double)parsoidList.size());
		parsoidList.add(url);
	}

}
