package wikiprocessor.dbconnector.service;

/**
 * 
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.06.25.
 *
 * Interface for DBConnector
 * 
 */
public interface DBConnectorService {
	/**
	 * insert text into database
	 * 
	 * @param text to insert
	 */
	public void insert(String text);
}
