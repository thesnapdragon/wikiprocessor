package wikiprocessor.dbconnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import wikiprocessor.dbconnector.service.DBConnectorService;
import wikiprocessor.logger.service.LoggerService;

/**
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.07.22.
 *
 * Activator class for DBConnector
 * 
 * manages DBConnector's lifecycle
 */
public class DBConnectorActivator implements BundleActivator {

	// constants for handling DB
	private static final String DBUSER = "wikiprocessor";
	private static final String DBPASSWORD = "^tcp7yAFZ@PrZ)k-!3ug";
	private static final String DB_CONNECTIONSTRING = "wikiprocessor.dbactivator.connectionstring";
	
	// logger instance
	public static LoggerService logger;
	
	// JDBC connection instance
	public static Connection conn;
	
	/**
	 * creates DB connection, DB admin user for WikiProcessor, and database table for articles
	 * when everything is done, registers DB service
	 */
	@Override
	public void start(BundleContext context) {
		// gets Logger instance
        ServiceReference logsref = context.getServiceReference(LoggerService.class.getName());
        logger = (LoggerService) context.getService(logsref);
		
		try {
			// creating connection
			Class.forName("org.h2.Driver");
			Properties prop = new Properties();
			prop.put("user", DBUSER);
			prop.put("password", DBPASSWORD);
			// getting connection string property from Felix
			String connstring = context.getProperty(DB_CONNECTIONSTRING);
			if (connstring == null || connstring.isEmpty()) {
				System.err.println("WARNING! Can not find DB's connection string property, using default value!");
				logger.warn("Can not find DB's connection string property, using default value!");
				// default value
				connstring = "jdbc:h2:tcp://localhost:9123/";
			}
	        conn = DriverManager.getConnection(connstring, prop);
	        conn.setAutoCommit(true);
	        Statement stat = conn.createStatement();
	       
	        // creating a user and deleting default
	        stat.execute("CREATE USER IF NOT EXISTS " + DBUSER + " PASSWORD '" + DBPASSWORD + "' ADMIN");
	        stat.execute("DROP USER IF EXISTS SA");
	        // creating articles table
	        stat.execute("CREATE TABLE IF NOT EXISTS articles(text CLOB, length INT, title VARCHAR(255), inserted TIMESTAMP, revision INT)");
		} catch (ClassNotFoundException e) {
			logger.error("Can not found H2 JDBC driver!");
			e.printStackTrace();
		} catch (SQLException e) {
			logger.error("Error while creating DB user, or deleting default (SA) user, or creating Articles table!");
			e.printStackTrace();
		}
		
		// create service properties
        Hashtable<String, String> properties = new Hashtable<String, String>();
        properties.put("WikiProcessorModule", "DBConnectorService");
        // register service
        context.registerService(DBConnectorService.class.getName(), new DBConnector(), properties);
        
        logger.debug("Started: DBConnector bundle.");
	}

	/**
	 * unregisters DB service
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		logger.debug("Stopped: DBConnector bundle.");
		ServiceReference dbsref = context.getServiceReference(DBConnectorService.class.getName());
		context.ungetService(dbsref);
	}

}
