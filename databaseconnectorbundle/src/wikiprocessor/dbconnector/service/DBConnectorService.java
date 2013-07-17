package wikiprocessor.dbconnector.service;

import wikiprocessor.logger.util.Article;

/**
 * 
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.07.17.
 *
 * Interface for DBConnector
 * 
 */
public interface DBConnectorService {
	/**
	 * insert article into database
	 * 
	 * @param article to insert
	 */
	public void insert(Article article);
}
