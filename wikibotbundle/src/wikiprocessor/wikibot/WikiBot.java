package wikiprocessor.wikibot;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jibble.pircbot.Colors;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;

import wikiprocessor.logger.util.Article;
import wikiprocessor.parser.service.QueueManagerService;

/**
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.07.29.
 *
 * WikiBot
 * 
 * it gets data from IRC channel, and send to parser's queue
 */
public class WikiBot extends PircBot {

	// IRC server
	private static final String SERVERNAME = "irc.wikimedia.org";
	// IRC channel
	private static final String CHANNELNAME = "#en.wikipedia";
	// IRC BOT name
	private static final String BOTNAME = "WikiProcessorBot2";
	// QueueManager instance
	private QueueManagerService queue = null;
	// is IRC BOT connected?
	private boolean connected;
	
	/**
	 * WikiBot constructor
	 * 
	 * sets bot's name, attaches QueueManager, sets connection status to false
	 */
	public WikiBot(QueueManagerService qms) {
        setName(BOTNAME);
        // sets QueueManager instance
        queue = qms;
        connected = false;
    }
    
	/**
	 * WikiBot onMessage event listener
	 * 
	 * grep name of the new WikiPedia pages, and send to Parser
	 */
	@Override
    protected void onMessage(String channel, String sender, String login, String hostname, String message) {    	
    	// grep name of page from message
    	if (sender.equals("rc-pmtpa")) {
			Pattern pattern = Pattern.compile("\\[\\[(.*)\\]\\].*http.*");
			Matcher matcher = pattern.matcher(Colors.removeColors(message));
			
			if (matcher.find()) {
				String content = matcher.group(1);
				// ignore List, Category, Talk... containing colon
				if (!(content.matches(".*:.*") || content.matches(".*List of.*"))) {
					// content matched grep revision number
					Pattern revisionpattern = Pattern.compile(".*oldid=(\\d+).*");
					Matcher revisionmatcher = revisionpattern.matcher(Colors.removeColors(message));
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
    
    /**
     * overrided event listener, called when IRC BOT quits from channel
     */
    @Override
    protected void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
    	WikiBotActivator.logger.warn("WikiBot quited! Reason: " + reason);
    	WikiBotActivator.statistics.increaseWarningLogCount();
    }
    
    /**
     * overrided event listener, called when IRC BOT disconnects, IRC BOT will reconnect
     */
    @Override
    protected void onDisconnect() {
    	WikiBotActivator.logger.warn("WikiBot disconnected! Wait 1 minute to reconnect...");
    	WikiBotActivator.statistics.increaseWarningLogCount();
    	connected = false;
    	while (connected == false) {
    		onError();
    	}
    }
	
    /**
     * bot joins to IRC channel if not connected
     */
	public void start() {
		if (connected) return;
		
		// disable debugging output.
		setVerbose(false);
        
        // connect to the IRC server.
        try {
			this.connect(SERVERNAME);
			connected = true;
		} catch (NickAlreadyInUseException e) {
			WikiBotActivator.logger.error("IRC nick is already in use!");
			WikiBotActivator.statistics.increaseErrorLogCount();
			e.printStackTrace();
			if (connected) return;
			onError();
		} catch (IOException e) {
			WikiBotActivator.logger.error("Error while connecting to IRC server!");
			WikiBotActivator.statistics.increaseErrorLogCount();
			e.printStackTrace();
			if (connected) return;
			onError();
		} catch (IrcException e) {
			WikiBotActivator.logger.error("Error in IRC connection!");
			WikiBotActivator.statistics.increaseErrorLogCount();
			e.printStackTrace();
			if (connected) return;
			onError();
		}

        // Join the #en.wikipedia channel.
        joinChannel(CHANNELNAME);
	}
	
	/**
	 * called on IRC error
	 */
	public void onError() {
		try {
			// wait 1 min
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			WikiBotActivator.logger.error("WikiBot's sleep has interrupted!");
			WikiBotActivator.statistics.increaseErrorLogCount();
			e.printStackTrace();
		}
		start();
	}
    
}
