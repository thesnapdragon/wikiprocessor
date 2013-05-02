package wikiprocessor.wikibot;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import wikiprocessor.parser.service.QueueManagerService;

/**
 * 
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.04.25.
 *
 * Activator class for WikiBot
 * 
 * manages WikiBot's lifecycle
 */
public class WikiBotActivator implements BundleActivator {
	
	/**
	 * starts WikiBot
	 */
    public void start(BundleContext context){
        System.out.println("Starting IRC bot bundle.");        
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
        System.out.println("Stopping IRC bot bundle.");
    }
}