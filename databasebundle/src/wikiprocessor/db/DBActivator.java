package wikiprocessor.db;

import org.h2.tools.Server;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * 
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.06.25.
 *
 * Activator class for H2 Database
 * 
 * manages DB's lifecycle
 */
public class DBActivator implements BundleActivator {

	private static final String BASEDIR = "/tmp/wikiprocessor/test";
	
	@Override
	public void start(BundleContext arg0) throws Exception {
		System.out.println("Starting DB bundle.");
		// starting TCP server with specified TCP port and DB location
		Server server = Server.createTcpServer(new String[]{"-tcpPort" , "9123" , "-tcpAllowOthers", "-baseDir", BASEDIR});
		server.start();
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		System.out.println("Stopping DB bundle.");
	}

}