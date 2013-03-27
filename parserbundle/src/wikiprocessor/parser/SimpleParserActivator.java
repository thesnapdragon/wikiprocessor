package wikiprocessor.parser;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * 
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.03.27.
 *
 * Activator class for SztakipediaParser
 * 
 * manages Parser's lifecycle
 */
public class SimpleParserActivator implements BundleActivator{
	
	/**
	 * starts Parser
	 */
    public void start(BundleContext context){
        System.out.println("Starting parser bundle.");
    }

    /**
     * stops Parser
     */
    public void stop(BundleContext context){
        System.out.println("Stopping parser bundle.");
    }
}