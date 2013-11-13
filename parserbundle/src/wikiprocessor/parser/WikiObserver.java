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
 * @since 2013.10.25.
 * 
 * observes QueueManager and handles Wikiworker
 */
public class WikiObserver implements Observer {
	
	// DB bundle's service
	private DBConnectorService database;
	
	private ExecutorService executor = null;
	private LinkedBlockingQueue<String> parsoidList = null;
	private int parsoidMaxNumber = 0;
	
	private String name = null;
	
	/**
	 * stores DB bundle's interface
	 * @param db DB bundle's service instance
	 * @param parsoidAddressList 
	 */
	public WikiObserver(String name, DBConnectorService db, String[] parsoidAddressList) {
		this.name = name;
		
		this.parsoidList = new LinkedBlockingQueue<String>(Arrays.asList(parsoidAddressList));
		this.parsoidMaxNumber = parsoidList.size();
	
		this.database = db;
		this.executor = Executors.newFixedThreadPool(parsoidList.size());
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
		ParserActivator.statistics.setWorkingParserRatio(name, (parsoidMaxNumber - parsoidList.size()) / (double)parsoidMaxNumber);
		return parsoidList.poll();
	}

	public synchronized void putParsoidInstance(String url) {
		ParserActivator.statistics.setWorkingParserRatio(name, (parsoidMaxNumber - parsoidList.size()) / (double)parsoidMaxNumber);
		parsoidList.add(url);
	}

	public String getName() {
		return name;
	}
}
