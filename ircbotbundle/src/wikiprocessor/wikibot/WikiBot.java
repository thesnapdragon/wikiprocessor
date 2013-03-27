package wikiprocessor.wikibot;

import java.io.IOException;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;

/**
 * 
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.03.27.
 *
 * WikiBot
 * 
 * it tells the time
 */
public class WikiBot extends PircBot {

	// IRC server
	String serverName;
	// IRC channel
	String channelName;
	
	/**
	 * WikiBot constructor
	 * 
	 * sets IRC datas, bot's name
	 */
	public WikiBot() {
		serverName = "irc.wikimedia.org";
		channelName = "#en.wikipedia";
        setName("WikiBot");
    }
    
	/**
	 * when asked, it tells the time
	 */
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
		if (message.equalsIgnoreCase("time")) {
			String time = new java.util.Date().toString();
			sendMessage(channel, sender + ": The time is now " + time);
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
