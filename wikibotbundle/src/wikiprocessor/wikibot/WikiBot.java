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
 * @since 2013.07.17.
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
	
	/**
	 * WikiBot constructor
	 * 
	 * sets bot's name, attaches QueueManager
	 */
	public WikiBot(QueueManagerService qms) {
        setName(BOTNAME);
        // sets QueueManager instance
        queue = qms;
    }
    
	/**
	 * WikiBot onMessage event listener
	 * 
	 * grep name of the new WikiPedia pages, and send to SimpleParser
	 * 
	 */
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
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
     * bot joins to IRC channel
     */
	public void start() {
        // disable debugging output.
		setVerbose(false);
        
        // connect to the IRC server.
        try {
			this.connect(SERVERNAME);
		} catch (NickAlreadyInUseException e) {
			WikiBotActivator.logger.error("IRC nick is already in use!");
			e.printStackTrace();
		} catch (IOException e) {
			WikiBotActivator.logger.error("Error while connecting to IRC server!");
			e.printStackTrace();
		} catch (IrcException e) {
			WikiBotActivator.logger.error("Error in IRC connection!");
			e.printStackTrace();
		}

        // Join the #en.wikipedia channel.
        joinChannel(CHANNELNAME);
	}
    
}
