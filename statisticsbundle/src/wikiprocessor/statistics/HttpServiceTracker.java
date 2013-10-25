package wikiprocessor.statistics;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;

import wikiprocessor.statistics.view.StatisticsView;

/**
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.07.23.
 *
 * HttpServiceTracker register servlets
 */
@SuppressWarnings("rawtypes")
public class HttpServiceTracker extends ServiceTracker {
	
	private final static String APIURL = "/api";
	private final static String VIEWURL = "/statistics";
	
	/**
	 * call super's constructor
	 * @param context BundleContext
	 */
	@SuppressWarnings("unchecked")
	public HttpServiceTracker(BundleContext context) {
	    super(context, HttpService.class.getName(), null);
	}
	
	/**
	 * registering servlets
	 */
	@SuppressWarnings("unchecked")
	public Object addingService(ServiceReference reference) {
	    HttpService httpService = (HttpService) super.addingService(reference);
	    if (httpService == null) {
	    	return null;
	    }
	 
	    try {
	    	// registering api servlet
	    	httpService.registerServlet(APIURL, new WikiprocessorAPI(), null, null);
	    	
	    	httpService.registerServlet(VIEWURL, new StatisticsView(), null, null);
	    	
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	 
	    return httpService;
	}
	
	/**
	 * unregistering servlets
	 */
	@SuppressWarnings("unchecked")
	public void removedService(ServiceReference reference, Object service) {
	    HttpService httpService = (HttpService) service;
	    httpService.unregister(APIURL);
	 
	    super.removedService(reference, service);
	}
}
