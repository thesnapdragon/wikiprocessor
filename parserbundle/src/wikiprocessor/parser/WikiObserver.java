package wikiprocessor.parser;

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
 * @since 2013.10.18.
 * 
 * observes QueueManager and handles Wikiworker
 */
public class WikiObserver implements Observer {
	
	// Parsoid parsers count
	private final int WORKER_COUNT = 26;
	
	// DB bundle's service
	private DBConnectorService database;
	
	private ExecutorService executor = null;
	
	private LinkedBlockingQueue<String> parsoidList = new LinkedBlockingQueue<String>();
	
	/**
	 * stores DB bundle's interface
	 * @param db DB bundle's service instance
	 */
	public WikiObserver(DBConnectorService db) {
		parsoidList.add("http://192.168.200.178:8081");
		parsoidList.add("http://192.168.200.169:8081");
		parsoidList.add("http://192.168.200.170:8081");
		parsoidList.add("http://192.168.200.177:8081");
		parsoidList.add("http://192.168.200.191:8081");
		parsoidList.add("http://192.168.200.193:8081");
		parsoidList.add("http://192.168.200.183:8081");
		parsoidList.add("http://192.168.200.185:8081");
		parsoidList.add("http://192.168.200.176:8081");
		parsoidList.add("http://192.168.200.171:8081");
		parsoidList.add("http://192.168.200.175:8081");
		parsoidList.add("http://192.168.200.184:8081");
		parsoidList.add("http://192.168.200.174:8081");
		parsoidList.add("http://192.168.200.173:8081");
		parsoidList.add("http://192.168.200.192:8081");
		parsoidList.add("http://192.168.200.189:8081");
		parsoidList.add("http://192.168.200.179:8081");
		parsoidList.add("http://192.168.200.181:8081");
		parsoidList.add("http://192.168.200.186:8081");
		parsoidList.add("http://192.168.200.182:8081");
		parsoidList.add("http://192.168.200.172:8081");
		parsoidList.add("http://192.168.200.187:8081");
		parsoidList.add("http://192.168.201.31:8081");
		parsoidList.add("http://192.168.200.188:8081");
		parsoidList.add("http://192.168.200.180:8081");
		parsoidList.add("http://192.168.200.190:8081");
	
		this.database = db;
		executor = Executors.newFixedThreadPool(WORKER_COUNT);
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
		ParserActivator.statistics.setWorkingParserRatio(parsoidList.size() / (double)WORKER_COUNT);
		return parsoidList.poll();
	}
	
	public synchronized void putParsoidInstance(String url) {
		ParserActivator.statistics.setWorkingParserRatio(parsoidList.size() / (double)WORKER_COUNT);
		parsoidList.add(url);
	}

}
