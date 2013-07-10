package wikiprocessor.dbconnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import wikiprocessor.dbconnector.service.DBConnectorService;

/**
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.07.10.
 *
 * DBConnector implementation
 * 
 * handles database through JDBC API
 */
public class DBConnector implements DBConnectorService {

	/**
	 * insert text into database
	 * 
	 * @param text to insert
	 */
	@Override
	public void insert(String text) {
		// get JDBC connection from DBConnectorActivator
		Connection conn = DBConnectorActivator.conn;
		
		// create a PreparedStatement to put something into DB
        PreparedStatement ps;
        try {
			ps = conn.prepareStatement("INSERT INTO articles VALUES(?,?)");
			// first parameter is article's text (CLOB)
			ps.setString(1, text);
			// second parameter is length of article's text (INT)
			ps.setInt(2, text.length());
			ps.execute();
			DBConnectorActivator.logger.trace("Article inserted!");
		} catch (SQLException e) {
			DBConnectorActivator.logger.error("Can not insert into H2 database!");
			e.printStackTrace();
		}
	}
}
