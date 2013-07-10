package wikiprocessor.db;

import org.h2.tools.Server;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * 
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.07.08.
 *
 * Activator class for H2 Database
 * 
 * manages DB's lifecycle
 */
public class DBActivator implements BundleActivator {
	
	// DB server instance
	private Server server;
	
	/**
	 * starts H2 database with specified options
	 */
	@Override
	public void start(BundleContext arg0) throws Exception {
		System.out.println("Starting DB bundle.");
		// getting basedir property from Felix
		String basedir = arg0.getProperty("wikiprocessor.dbactivator.basedir");
		if (basedir.isEmpty() || basedir.equals(null)) {
			System.err.println("ERROR! Can not find DB's basedir property!");
		}
		// starting TCP server with specified TCP port and DB location
		server = Server.createTcpServer(new String[]{"-tcpPort" , "9123" , "-tcpAllowOthers", "-baseDir", basedir});
		server.start();
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		System.out.println("Stopping DB bundle.");
		server.stop();
	}

}
