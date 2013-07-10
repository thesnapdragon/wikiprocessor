package wikiprocessor.parser;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import wikiprocessor.dbconnector.service.DBConnectorService;
import wikiprocessor.logger.service.LoggerService;
import wikiprocessor.parser.service.QueueManagerService;

/**
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.07.10.
 *
 * Activator class for SztakipediaParser
 * 
 * manages Parser's lifecycle
 */
public class SimpleParserActivator implements BundleActivator {
	
	// logger instance
	private static LoggerService logger;
	
	/**
	 * starts Parser
	 */
    public void start(BundleContext context){
    	// gets Logger instance
        ServiceReference logsref = context.getServiceReference(LoggerService.class.getName());
        logger = (LoggerService) context.getService(logsref);
        logger.debug("Starting parser bundle.");
        
        // create service properties
        Hashtable<String, String> properties = new Hashtable<String, String>();
        properties.put("WikiProcessorModule", "QueueManagerService");
        QueueManager queuemanager = new QueueManager();
        
        // gets DBConnector instance
        ServiceReference dbsref = context.getServiceReference(DBConnectorService.class.getName());
        DBConnectorService database = (DBConnectorService) context.getService(dbsref);
        
        // add observer to QueueManager
        WikiDownloader wikidownloader = new WikiDownloader(database);
        queuemanager.addObserver(wikidownloader);
        // register service
        context.registerService(QueueManagerService.class.getName(), queuemanager, properties);
    }

    /**
     * stops Parser
     */
    public void stop(BundleContext context){
        logger.debug("Stopping parser bundle.");
    }
}