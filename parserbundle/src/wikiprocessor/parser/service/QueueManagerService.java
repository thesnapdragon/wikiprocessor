package wikiprocessor.parser.service;

import wikiprocessor.logger.util.Article;

/**
 * 
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.07.17.
 *
 * Interface for QueueManager
 * 
 */
public interface QueueManagerService {
	/**
	 * add item to parser's queue
	 * 
	 * @param item item to add
	 */
	public void addToQueue(Article item);
}
