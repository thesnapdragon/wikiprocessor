package wikiprocessor.parser;

import java.util.Observable;
import java.util.concurrent.LinkedBlockingQueue;

import wikiprocessor.logger.util.Article;
import wikiprocessor.parser.service.QueueManagerService;

/**
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.08.12.
 * 
 * QueueManager implementation
 */
public class QueueManager extends Observable implements QueueManagerService {

	// threadsafe queue
	private LinkedBlockingQueue<Article> queue = null;
	
	public QueueManager(int size) {
		queue =  new LinkedBlockingQueue<Article>(size);
	}
	
	/**
	 * add item to the queue
	 * @param item item to add
	 */
	public void addToQueue(Article item) {
		// add item to the queue
		try {
			queue.put(item);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ParserActivator.statistics.increaseQueueLength();
		// notifies WikiObserver
		setChanged();
		notifyObservers();
	}
	
	/**
	 * get item from queue
	 * @return item
	 */
	public Article pollFromQueue() {
		ParserActivator.statistics.decreaseQueueLength();
		return queue.poll();
	}
	
	/**
	 * getter of queue
	 * @return queue
	 */
	public LinkedBlockingQueue<Article> getQueue() {
		return queue;
	}

	/**
	 * setter of queue
	 * @param queue
	 */
	public void setQueue(LinkedBlockingQueue<Article> queue) {
		this.queue = queue;
	}
	
	/**
	 * getter of queue's size
	 * @return queue's size
	 */
	public int getSize() {
		return queue.size();
	}

}
