package wikiprocessor.dbconnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import wikiprocessor.dbconnector.service.DBConnectorService;
import wikiprocessor.logger.util.Article;

/**
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.07.17.
 *
 * DBConnector implementation
 * 
 * handles database through JDBC API
 */
public class DBConnector implements DBConnectorService {

	/**
	 * insert article into database
	 * 
	 * @param article to insert
	 */
	@Override
	public void insert(Article article) {
		// get JDBC connection from DBConnectorActivator
		Connection conn = DBConnectorActivator.conn;
		
		// create a PreparedStatement to put something into DB
        PreparedStatement ps;
        try {
			ps = conn.prepareStatement("INSERT INTO articles VALUES(?,?,?,?,?)");
			// first parameter is article's text (CLOB)
			ps.setString(1, article.getText());
			// second parameter is length of article's text (INT)
			ps.setInt(2, article.getText().length());
			// third parameter is title
			ps.setString(3, article.getTitle());
			// fourth parameter is sysdate
			Date sysdate = new Date();
			Timestamp t = new Timestamp(sysdate.getTime());
			ps.setTimestamp(4, t);
			// fifth parameter is revisionnumber
			ps.setInt(5, article.getRevision());			
			ps.execute();
			DBConnectorActivator.logger.trace("Article inserted: " + article.getTitle());
		} catch (SQLException e) {
			DBConnectorActivator.logger.error("Can not insert into H2 database!");
			e.printStackTrace();
		}
	}
}
