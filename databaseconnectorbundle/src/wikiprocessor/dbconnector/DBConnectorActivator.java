package wikiprocessor.dbconnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import wikiprocessor.dbconnector.service.DBConnectorService;

/**
 * 
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.06.25.
 *
 * Activator class for DBConnector
 * 
 * manages DBConnector's lifecycle
 */
public class DBConnectorActivator implements BundleActivator {

	private static final String DBURL = "jdbc:h2:tcp://localhost:9123/";
	private static final String DBUSER = "wikiprocessor";
	private static final String DBPASSWORD = "^tcp7yAFZ@PrZ)k-!3ug";
	
	public static Connection conn;
	
	@Override
	public void start(BundleContext context) {
		System.out.println("Starting DBConnector bundle.");
		
		try {
			// creating connection
			Class.forName("org.h2.Driver");
			Properties prop = new Properties();
			prop.put("user", DBUSER);
			prop.put("password", DBPASSWORD);
	        conn = DriverManager.getConnection(DBURL, prop);
	        Statement stat = conn.createStatement();
	       
	        // creating a user and delete default
	        stat.execute("CREATE USER IF NOT EXISTS " + DBUSER + " PASSWORD '" + DBPASSWORD + "' ADMIN");
	        stat.execute("DROP USER IF EXISTS SA");
	        // creating articles table
	        stat.execute("CREATE TABLE IF NOT EXISTS articles(text CLOB, length INT)");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// create service properties
        Hashtable<String, String> properties = new Hashtable<String, String>();
        properties.put("WikiProcessorModule", "DBConnectorService");
        // register service
        context.registerService(DBConnectorService.class.getName(), new DBConnector(), properties);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		System.out.println("Stopping DBConnector bundle.");		
	}

}
