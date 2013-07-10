package wikiprocessor.wikibot;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import wikiprocessor.logger.service.LoggerService;
import wikiprocessor.parser.service.QueueManagerService;

/**
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.07.10.
 *
 * Activator class for WikiBot
 * 
 * manages WikiBot's lifecycle
 */
public class WikiBotActivator implements BundleActivator {
	
	// logger instance
	private static LoggerService logger;
	
	/**
	 * starts WikiBot
	 */
    public void start(BundleContext context){
    	// gets Logger instance
        ServiceReference logsref = context.getServiceReference(LoggerService.class.getName());
        logger = (LoggerService) context.getService(logsref);
        logger.debug("Starting IRC bot bundle.");      
        // gets QueueManager instance
        ServiceReference qmsref = context.getServiceReference(QueueManagerService.class.getName());
        QueueManagerService queue = (QueueManagerService) context.getService(qmsref);
        // starting bot
        WikiBot bot = new WikiBot(queue);
        bot.start();
    }

    /**
     * stops WikiBot
     */
    public void stop(BundleContext context){
        logger.debug("Stopping IRC bot bundle.");
    }
}