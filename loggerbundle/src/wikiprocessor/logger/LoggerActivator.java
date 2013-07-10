package wikiprocessor.logger;

import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import wikiprocessor.logger.service.LoggerService;

/**
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.07.10.
 *
 * Activator class for Logger
 * 
 * manages Logger's lifecycle
 */
public class LoggerActivator implements BundleActivator, LoggerService {

	// system property, that contains log4j configuration
	private static final String LOG4J_PROPERTIES = "wikiprocessor.log4j.properties";
	
	// logger instance
	private static Logger logger;
	
	@Override
	public void start(BundleContext context) throws Exception {
		// setting log4j.properties 
	    String log4jpropfile = context.getProperty(LOG4J_PROPERTIES);
	    if (log4jpropfile == null || log4jpropfile.isEmpty()) {
			System.err.println("WARNING! Can not find log4j properties file, using default value!");
			// default value
			log4jpropfile = "/opt/wikiprocessor/log4j.properties";
		}
	    PropertyConfigurator.configure(log4jpropfile);
		logger = Logger.getLogger(LoggerActivator.class);
		
		// create service properties
        Hashtable<String, String> properties = new Hashtable<String, String>();
        properties.put("WikiProcessorModule", "LoggerService");
        
        // register service
        context.registerService(LoggerService.class.getName(), this, properties);
        
        logger.debug("Started: Logger bundle.");
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		logger.debug("Stopped: Logger bundle.");
	}

	@Override
	public void trace(String msg) {
		logger.trace(msg);		
	}

	@Override
	public void debug(String msg) {
		logger.debug(msg);
	}

	@Override
	public void info(String msg) {
		logger.info(msg);
	}

	@Override
	public void warn(String msg) {
		logger.warn(msg);
	}

	@Override
	public void error(String msg) {
		logger.error(msg);
	}

	@Override
	public void fatal(String msg) {
		logger.fatal(msg);
	}

}
