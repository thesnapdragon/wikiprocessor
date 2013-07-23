package wikiprocessor.statistics;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wikiprocessor.statistics.data.StatisticsData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.07.23.
 *
 * Servlet that implements statistics API to Wikiprocessor
 */
public class WikiprocessorAPI extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, Integer> datasNow = new HashMap<String, Integer>();
		datasNow.put("eltárolt cikkek száma", StatisticsActivator.statistics.getStoredArticlesCount());
		datasNow.put("frissített cikkek száma", StatisticsActivator.statistics.getUpdatedArticlesCount());
		datasNow.put("újonnan beillesztett cikkek száma", StatisticsActivator.statistics.getInsertedArticlesCount());
		datasNow.put("nem feldolgozott cikkek száma", StatisticsActivator.statistics.getNotProcessedArticlesCount());

		Gson gson = new GsonBuilder().create();
		
		resp.setContentType("application/json");
		resp.getWriter().write(gson.toJson(datasNow));
	}

}