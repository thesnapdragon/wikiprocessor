package wikiprocessor.wikibot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.DisconnectEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.QuitEvent;

import wikiprocessor.logger.util.Article;
import wikiprocessor.parser.service.QueueManagerService;

/**
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 2.0
 * @since 2013.09.26.
 *
 * WikiBot
 * 
 * it gets data from IRC channel, and send to parser's queue
 */
@SuppressWarnings("rawtypes")
public class WikiBot extends ListenerAdapter {

	// QueueManager instance
	private QueueManagerService queue = null;
	
	/**
	 * WikiBot constructor
	 * 
	 * sets bot's name, attaches QueueManager, sets connection status to false
	 */
	public WikiBot(QueueManagerService qms) {
        // sets QueueManager instance
        queue = qms;
    }
    
	/**
	 * WikiBot onMessage event listener
	 * 
	 * grep name of the new WikiPedia pages, and send to Parser
	 */
	public void onMessage(MessageEvent event) {
    	// grep name of page from message
    	if (event.getUser().getNick().equals("rc-pmtpa")) {
			Pattern pattern = Pattern.compile("\\[\\[(.*)\\]\\].*http.*");
			Matcher matcher = pattern.matcher(Colors.removeColors(event.getMessage()));
			
			if (matcher.find()) {
				String content = matcher.group(1);
				// ignore List, Category, Talk... containing colon
				if (!(content.matches(".*:.*") || content.matches(".*List of.*"))) {
					// content matched grep revision number
					Pattern revisionpattern = Pattern.compile(".*oldid=(\\d+).*");
					Matcher revisionmatcher = revisionpattern.matcher(Colors.removeColors(event.getMessage()));
					if (revisionmatcher.find()) {
						int revision = Integer.parseInt(revisionmatcher.group(1));
						// add pagename and revision to the parser's queue
						WikiBotActivator.logger.trace("WikiBot grepped: " + content + " " + revision);
						queue.addToQueue(new Article(content, revision));
					}
				}
			}
		}
	}
    
	public void onQuit(QuitEvent event) {
    	WikiBotActivator.logger.warn("WikiBot quited! Reason: " + event.getReason());
    	WikiBotActivator.statistics.increaseWarningLogCount();
    }
    
    public void onDisconnect(DisconnectEvent event) {
    	WikiBotActivator.logger.warn("WikiBot disconnected!");
    	WikiBotActivator.statistics.increaseWarningLogCount();
    }
    
}
