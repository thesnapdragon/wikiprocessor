package wikiprocessor.parser;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import wikiprocessor.dbconnector.service.DBConnectorService;
import wikiprocessor.dump.DumpLoader;
import wikiprocessor.logger.service.LoggerService;
import wikiprocessor.parser.service.QueueManagerService;
import wikiprocessor.statistics.data.service.StatisticsDataService;

/**
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.10.25.
 *
 * Activator class for SztakipediaParser
 * 
 * manages Parser's lifecycle
 */
public class ParserActivator implements BundleActivator {
	
	// system property, that contains dump XML file path
	private static final String DUMP_FILEPATH_VARIABLE = "wikiprocessor.dump.filepath";
	
	private static final int QUEUELENGTH = 10000;
	
	// logger instance
	public static LoggerService logger;
	
	// statistics bundle instance
	public static StatisticsDataService statistics;
	
	// dump XML file path
	private String dumpFilePath;
	
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

        // gets DBConnector instance
        ServiceReference dbsref = context.getServiceReference(DBConnectorService.class.getName());
        DBConnectorService database = (DBConnectorService) context.getService(dbsref);
        
        // add observer to QueueManager
        String[] parsoidAddressList = {
        		"http://192.168.201.65:8081",
        		"http://192.168.201.57:8081",
        		"http://192.168.201.63:8081",
        		"http://192.168.201.55:8081",
        		"http://192.168.201.64:8081",
        		"http://192.168.201.44:8081",
        		"http://192.168.201.67:8081",
        		"http://192.168.201.59:8081",
        		"http://192.168.201.45:8081",
        		"http://192.168.201.43:8081",
        		"http://192.168.201.49:8081",
        		"http://192.168.201.53:8081",
        		"http://192.168.201.61:8081",
        		"http://192.168.201.51:8081",
        		"http://192.168.201.46:8081",
        		"http://192.168.201.47:8081",
        		"http://192.168.201.58:8081",
        		"http://192.168.201.68:8081",
        		"http://192.168.201.50:8081",
        		"http://192.168.201.54:8081",
        		"http://192.168.201.56:8081"
        };
        
        // create service properties
        Hashtable<String, String> properties = new Hashtable<String, String>();
        properties.put("WikiProcessorModule", "QueueManagerService");
        QueueManager queuemanager = new QueueManager(QUEUELENGTH);
        // create observer
        WikiObserver wikiObserver = new WikiObserver("main", database, parsoidAddressList);
        queuemanager.addObserver(wikiObserver);
        // register service
        context.registerService(QueueManagerService.class.getName(), queuemanager, properties);
        
        // dump loading
        // setting dump file path
	    dumpFilePath = context.getProperty(DUMP_FILEPATH_VARIABLE);
	    if (dumpFilePath == null || dumpFilePath.isEmpty()) {
	    	logger.warn("Can not find dump!");
	    }
	    loadDump(dumpFilePath, database);
        
        logger.debug("Started: Parser bundle.");
    }

    /**
     * stops Parser
     */
    public void stop(BundleContext context){
        logger.debug("Stopped: Parser bundle.");
    }
    
    /**
     * loading dump
     * @param dumpFilePath to load
     */
    public void loadDump(String dumpFilePath, DBConnectorService database) {
    	QueueManager dumpQueueManager = new QueueManager(QUEUELENGTH);
    	// add observer to dumpQueueManager
        String[] parsoidAddressList = {
        		"http://192.168.201.66:8081",
        		"http://192.168.201.62:8081",
        		"http://192.168.201.60:8081",
        		"http://192.168.201.52:8081",
        		"http://192.168.201.48:8081"
        		};
        WikiObserver dumpWikiObserver = new WikiObserver("dump", database, parsoidAddressList);
        dumpQueueManager.addObserver(dumpWikiObserver);
    	Thread loader = new Thread(new DumpLoader(dumpFilePath, dumpQueueManager));
    	loader.start();
    }
}