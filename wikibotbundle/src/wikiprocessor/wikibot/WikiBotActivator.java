package wikiprocessor.wikibot;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import wikiprocessor.logger.service.LoggerService;
import wikiprocessor.parser.service.QueueManagerService;
import wikiprocessor.statistics.data.service.StatisticsDataService;

/**
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.07.29.
 *
 * Activator class for WikiBot
 * 
 * manages WikiBot's lifecycle
 */
public class WikiBotActivator implements BundleActivator {
	
	// logger instance
	public static LoggerService logger;
	
	// statistics bundle unstance
	public static StatisticsDataService statistics;
	
	// Wikibot instance
	private WikiBot bot;
	
	/**
	 * starts WikiBot
	 */
    public void start(BundleContext context){
    	// gets Logger instance
        ServiceReference logsref = context.getServiceReference(LoggerService.class.getName());
        logger = (LoggerService) context.getService(logsref);
        
        // gets Statistics instance
        ServiceReference statsref = context.getServiceReference(StatisticsDataService.class.getName());
        statistics = (StatisticsDataService) context.getService(statsref);
              
        // gets QueueManager instance
        ServiceReference qmsref = context.getServiceReference(QueueManagerService.class.getName());
        QueueManagerService queue = (QueueManagerService) context.getService(qmsref);
        // starting bot
        bot = new WikiBot(queue);
        bot.start();
        
        logger.debug("Started: IRC bot bundle.");
        statistics.increaseDebugLogCount();
    }

    /**
     * stops WikiBot
     */
    public void stop(BundleContext context){
    	bot.disconnect();
        logger.debug("Stopped: IRC bot bundle.");
        statistics.increaseDebugLogCount();
    }
}