package wikiprocessor.statistics.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wikiprocessor.statistics.StatisticsActivator;

/**
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.07.23.
 *
 * Servlet that implements statistics API to Wikiprocessor
 */
public class StatisticsView extends HttpServlet {

	private static final long serialVersionUID = 7254157154095396493L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter pw = resp.getWriter();
		resp.setContentType("text/html");
		
		URL configURL = new URL("file://" + StatisticsActivator.statisticsFilePath);
	    if (configURL != null) {
	    	BufferedReader bf = new BufferedReader(new InputStreamReader(configURL.openStream()));
	        try {
	            String str = null;
	        	while ((str = bf.readLine()) != null) {
	        	     pw.write(str);
	        	   }
	        } finally {
	            bf.close();
	        }
	    }
	}

}
