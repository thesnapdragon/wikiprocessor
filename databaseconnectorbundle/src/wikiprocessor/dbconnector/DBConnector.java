package wikiprocessor.dbconnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import wikiprocessor.dbconnector.service.DBConnectorService;
import wikiprocessor.logger.util.Article;

/**
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.07.29.
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
			Timestamp timeStamp = new Timestamp(sysdate.getTime());
			ps.setTimestamp(4, timeStamp);
			// fifth parameter is revisionnumber
			ps.setInt(5, article.getRevision());			
			ps.execute();
			
			DBConnectorActivator.logger.trace("Article inserted: " + article.getTitle());
			DBConnectorActivator.statistics.increaseTraceLogCount();
		} catch (SQLException e) {
			DBConnectorActivator.logger.error("Can not insert into H2 database!");
			DBConnectorActivator.statistics.increaseErrorLogCount();
			e.printStackTrace();
		}
	}

	@Override
	public boolean searchOlder(String title, int revision){
		// get JDBC connection from DBConnectorActivator
		Connection conn = DBConnectorActivator.conn;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("SELECT title FROM articles WHERE title LIKE ? AND revision < ?");
			ps.setString(1, title);
			ps.setInt(2, revision);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			DBConnectorActivator.logger.error("Can not query H2 database!");
			DBConnectorActivator.statistics.increaseErrorLogCount();
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void update(Article article) {
		// get JDBC connection from DBConnectorActivator
		Connection conn = DBConnectorActivator.conn;
		
		// create a PreparedStatement to put something into DB
        PreparedStatement ps;
        try {
			ps = conn.prepareStatement("UPDATE articles SET text=?,length=?,inserted=?,revision=? WHERE title=?");
			ps.setString(1, article.getText());
			ps.setInt(2, article.getText().length());
			Date sysdate = new Date();
			Timestamp timeStamp = new Timestamp(sysdate.getTime());
			ps.setTimestamp(3, timeStamp);
			// fifth parameter is revisionnumber
			ps.setInt(4, article.getRevision());
			ps.setString(5, article.getTitle());
			ps.execute();
			
			DBConnectorActivator.logger.trace("Article updated: " + article.getTitle());
			DBConnectorActivator.statistics.increaseTraceLogCount();
		} catch (SQLException e) {
			DBConnectorActivator.logger.error("Can not update article in H2 database!");
			DBConnectorActivator.statistics.increaseErrorLogCount();
			e.printStackTrace();
		}		
	}

	@Override
	public boolean searchNewer(String title, int revision) {
		// get JDBC connection from DBConnectorActivator
		Connection conn = DBConnectorActivator.conn;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("SELECT title FROM articles WHERE title LIKE ? AND revision >= ?");
			ps.setString(1, title);
			ps.setInt(2, revision);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			DBConnectorActivator.logger.error("Can not query H2 database!");
			DBConnectorActivator.statistics.increaseErrorLogCount();
			e.printStackTrace();
		}
		return false;
	}
}
