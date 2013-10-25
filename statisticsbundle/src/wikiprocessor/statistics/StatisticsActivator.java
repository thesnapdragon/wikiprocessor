package wikiprocessor.statistics;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import wikiprocessor.statistics.data.StatisticsData;
import wikiprocessor.statistics.data.service.StatisticsDataService;

/**
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.07.23.
 *
 * Activator class for Statistics
 * 
 * manages Statistics module's lifecycle
 */
public class StatisticsActivator implements BundleActivator {

	// system property, that contains statistics HTML file path
	private static final String STATISTICS_FILEPATH_VARIABLE = "wikiprocessor.statistics.filepath";
	
	// statistics instance
	public static String statisticsFilePath;
	public static StatisticsData statistics;
	
	private HttpServiceTracker serviceTracker;
	
	/**
	 * instantiate a service tracker to register servlets
	 */
	@Override
	public void start(BundleContext context) throws Exception {
        // create a new service tracker
        serviceTracker = new HttpServiceTracker(context);
        serviceTracker.open();
        
        // setting statistics file path 
	    statisticsFilePath = context.getProperty(STATISTICS_FILEPATH_VARIABLE);
	    if (statisticsFilePath == null || statisticsFilePath.isEmpty()) {
			// TODO
			// default value
			statisticsFilePath = "/opt/wikiprocessor/web/index.html";
		}
	    
        // create service properties
        Hashtable<String, String> properties = new Hashtable<String, String>();
        properties.put("WikiProcessorModule", "StatisticsService");

        statistics = new StatisticsData();
		// register service
        context.registerService(StatisticsDataService.class.getName(), statistics , properties);
	}

	/**
	 * closing service tracker (unregisters servlets)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		serviceTracker.close();
	    serviceTracker = null;
	}

}
