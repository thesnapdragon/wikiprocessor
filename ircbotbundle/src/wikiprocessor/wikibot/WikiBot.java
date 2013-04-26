package wikiprocessor.wikibot;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jibble.pircbot.Colors;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;

import wikiprocessor.parser.service.QueueManagerService;

/**
 * 
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.04.25.
 *
 * WikiBot
 * 
 * it gets data from IRC channel, and send to parser's queue
 */
public class WikiBot extends PircBot {

	// IRC server
	private String serverName;
	// IRC channel
	private String channelName;
	// QueueManager instance
	private QueueManagerService queue = null;
	
	/**
	 * WikiBot constructor
	 * 
	 * sets IRC datas, bot's name
	 */
	public WikiBot(QueueManagerService qms) {
		// sets IRC parameters
		serverName = "irc.wikimedia.org";
		channelName = "#en.wikipedia";
        setName("WikiProcessorBot");
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
			Pattern pattern = Pattern.compile("\\[\\[(.*)\\]\\]");
			Matcher matcher = pattern.matcher(Colors.removeColors(message));
			
			if (matcher.find()) {
				String content = matcher.group(1);
				// ignore List, Category, Talk... containing colon
				if (!(content.matches(".*:.*") || content.matches(".*List of.*"))) {
					// add pagename to the parser's queue
					queue.addToQueue(content);
				}
			}
		}
	}
	
    /**
     * bot joins to IRC channel
     */
	public void start() {
        // Enable debugging output.
		setVerbose(true);
        
        // Connect to the IRC server.
        try {
			this.connect(serverName);
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

        // Join the #en.wikipedia channel.
        joinChannel(channelName);
	}
    
}
