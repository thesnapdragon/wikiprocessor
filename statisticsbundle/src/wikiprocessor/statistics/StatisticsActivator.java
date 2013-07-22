package wikiprocessor.statistics;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import wikiprocessor.logger.service.LoggerService;

/**
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.07.22.
 *
 * Activator class for Statistics
 * 
 * manages Statistics module's lifecycle
 */
public class StatisticsActivator implements BundleActivator {

	// logger instance
	private static LoggerService logger;
	private HttpServiceTracker serviceTracker;
	
	@Override
	public void start(BundleContext context) throws Exception {
		// gets Logger instance
        ServiceReference logsref = context.getServiceReference(LoggerService.class.getName());
        logger = (LoggerService) context.getService(logsref);
		
        serviceTracker = new HttpServiceTracker(context);
        serviceTracker.open();
        
        
        logger.debug("Started: Statistics bundle.");
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		logger.debug("Stopped: Statistics bundle.");
		
		serviceTracker.close();
	    serviceTracker = null;
	}

}
