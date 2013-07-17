package wikiprocessor.parser;

import java.util.Observable;
import java.util.concurrent.LinkedBlockingQueue;

import wikiprocessor.logger.util.Article;
import wikiprocessor.parser.service.QueueManagerService;

/**
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.07.17.
 * 
 * QueueManager implementation
 */
public class QueueManager extends Observable implements QueueManagerService {

	// threadsafe queue
	private LinkedBlockingQueue<Article> queue = new LinkedBlockingQueue<Article>();
	
	/**
	 * add item to the queue
	 * 
	 * @param item item to add
	 */
	public void addToQueue(Article item) {
		// add item to the queue
		queue.add(item);
		// notifies WikiDownloader
		setChanged();
		notifyObservers();
	}
	
	/**
	 * get item from queue
	 * 
	 * @return item
	 */
	public Article pollFromQueue() {
		return queue.poll();
	}
	
	/**
	 * getter of queue
	 * 
	 * @return queue
	 */
	public LinkedBlockingQueue<Article> getQueue() {
		return queue;
	}

	/**
	 * setter of queue
	 * 
	 * @param queue
	 */
	public void setQueue(LinkedBlockingQueue<Article> queue) {
		this.queue = queue;
	}

}
