package wikiprocessor.parser;

import java.util.concurrent.LinkedBlockingQueue;

import wikiprocessor.parser.service.QueueManagerService;

/**
 * 
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.03.27.
 *
 * QueueManager implementation
 * 
 */
public class QueueManager implements QueueManagerService {

	// threadsafe queue
	private LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();

	/**
	 * add item to the queue
	 */
	@Override
	public void addToQueue(String item) {
		// add item to the queue
		queue.add(item);
		System.out.println("Hozzáadva a Queue-hoz: "+item);
	}
	
	public LinkedBlockingQueue<String> getQueue() {
		return queue;
	}

	public void setQueue(LinkedBlockingQueue<String> queue) {
		this.queue = queue;
	}

}
