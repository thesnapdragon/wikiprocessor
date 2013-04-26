package wikiprocessor.parser;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import wikiprocessor.parser.service.QueueManagerService;

/**
 * 
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.04.26.
 *
 * Activator class for SztakipediaParser
 * 
 * manages Parser's lifecycle
 */
public class SimpleParserActivator implements BundleActivator {
	
	/**
	 * starts Parser
	 */
    public void start(BundleContext context){
        System.out.println("Starting parser bundle.");
        // create service properties
        Hashtable<String, String> properties = new Hashtable<String, String>();
        properties.put("WikiProcessorModule", "QueueManagerService");
        QueueManager queuemanager = new QueueManager();
        // add observer to QueueManager
        WikiDownloader wikidownloader = new WikiDownloader();
        queuemanager.addObserver(wikidownloader);
        // register service
        context.registerService(QueueManagerService.class.getName(), queuemanager, properties);
    }

    /**
     * stops Parser
     */
    public void stop(BundleContext context){
        System.out.println("Stopping parser bundle.");
    }
}