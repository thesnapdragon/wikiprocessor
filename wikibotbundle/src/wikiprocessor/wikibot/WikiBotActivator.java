package wikiprocessor.wikibot;

import java.io.IOException;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.exception.NickAlreadyInUseException;

import wikiprocessor.logger.service.LoggerService;
import wikiprocessor.parser.service.QueueManagerService;
import wikiprocessor.statistics.data.service.StatisticsDataService;

/**
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 2.0
 * @since 2013.09.26.
 *
 * Activator class for WikiBot
 * 
 * manages WikiBot's lifecycle
 */
public class WikiBotActivator implements BundleActivator {
	
	// IRC server
	private static final String SERVERNAME = "irc.wikimedia.org";
	// IRC channel
	private static final String CHANNELNAME = "#en.wikipedia";
	// IRC BOT name
	private static final String BOTNAME = "WikiProcessorBot2";
	
	// logger instance
	public static LoggerService logger;
	
	// statistics bundle unstance
	public static StatisticsDataService statistics;
	
	// PircBotX instance
	private PircBotX bot;
	
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
        bot = new PircBotX();
        bot.getListenerManager().addListener(new WikiBot(queue));
        bot.setName(BOTNAME);
        try {
			bot.connect(SERVERNAME);
		} catch (NickAlreadyInUseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IrcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        bot.joinChannel(CHANNELNAME);
        
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