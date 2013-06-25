package wikiprocessor.dbconnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import wikiprocessor.dbconnector.service.DBConnectorService;

public class DBConnector implements DBConnectorService {

	/**
	 * insert text into database
	 * 
	 * @param text to insert
	 */
	@Override
	public void insert(String text) {
		Connection conn = DBConnectorActivator.conn;
		
        PreparedStatement ps;
        try {
			ps = conn.prepareStatement("INSERT INTO articles VALUES(?,?)");
			ps.setString(1, text);
			ps.setInt(2, text.length());
			ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//        ResultSet rs = stat.executeQuery("SELECT * FROM articles");
//        while(rs.next()) {
//        	System.out.println(rs.getString(1));
//        }
	}
	
}
