package wikiprocessor.dbconnector.service;

import wikiprocessor.logger.util.Article;

/**
 * 
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.07.22.
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
	
	/**
	 * search articles by title
	 * 
	 * @param title article's title
	 * @param revision article's revision number
	 * @return true if article's found, false if not
	 */
	public boolean searchOlder(String title, int revision);
	
	/**
	 * update article in database
	 * 
	 * @param article new data to update
	 */
	public void update(Article article);
}
