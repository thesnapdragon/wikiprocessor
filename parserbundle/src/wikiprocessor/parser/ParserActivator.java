package wikiprocessor.parser;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import wikiprocessor.dbconnector.service.DBConnectorService;
import wikiprocessor.logger.service.LoggerService;
import wikiprocessor.parser.service.QueueManagerService;
import wikiprocessor.statistics.data.service.StatisticsDataService;

/**
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.07.29.
 *
 * Activator class for SztakipediaParser
 * 
 * manages Parser's lifecycle
 */
public class ParserActivator implements BundleActivator {
	
	// parser's connection url
	private static final String PARSER_CONNECTIONSTRING = "wikiprocessor.parsoid.connectionstring";
	
	// logger instance
	public static LoggerService logger;
	
	// statistics bundle instance
	public static StatisticsDataService statistics;
	
	// parsoid url address
	public static String parsoidUrl = null;
	
	/**
	 * starts Parser
	 */
    public void start(BundleContext context){
    	// gets Logger instance
        ServiceReference logsref = context.getServiceReference(LoggerService.class.getName());
        logger = (LoggerService) context.getService(logsref);
        
        // gets Statistics instance
        ServiceReference statsref = context.getServiceReference(StatisticsDataService.class.getName());
        statistics = (StatisticsDataService) context.getService(statsref);
        
        // create service properties
        Hashtable<String, String> properties = new Hashtable<String, String>();
        properties.put("WikiProcessorModule", "QueueManagerService");
        QueueManager queuemanager = new QueueManager();

        // gets DBConnector instance
        ServiceReference dbsref = context.getServiceReference(DBConnectorService.class.getName());
        DBConnectorService database = (DBConnectorService) context.getService(dbsref);
        
        // add observer to QueueManager
        WikiObserver wikiObserver = new WikiObserver(database);
        queuemanager.addObserver(wikiObserver);
        // register service
        context.registerService(QueueManagerService.class.getName(), queuemanager, properties);
        
        parsoidUrl = context.getProperty(PARSER_CONNECTIONSTRING);
        
        logger.debug("Started: Parser bundle.");
        statistics.increaseDebugLogCount();
    }

    /**
     * stops Parser
     */
    public void stop(BundleContext context){
        logger.debug("Stopped: Parser bundle.");
        statistics.increaseDebugLogCount();
    }
}