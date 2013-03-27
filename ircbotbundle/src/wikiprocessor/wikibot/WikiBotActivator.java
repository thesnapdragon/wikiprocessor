package wikiprocessor.wikibot;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * 
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.03.27.
 *
 * Activator class for WikiBot
 * 
 * manages WikiBot's lifecycle
 */
public class WikiBotActivator implements BundleActivator{
	
	/**
	 * starts WikiBot
	 */
    public void start(BundleContext context){
        System.out.println("Starting IRC bot bundle.");
        WikiBot bot=new WikiBot();
        bot.start();
    }

    /**
     * stops WikiBot
     */
    public void stop(BundleContext context){
        System.out.println("Stopping IRC bot bundle.");
    }
}