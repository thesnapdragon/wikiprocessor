package wikiprocessor.statistics;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

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
		JsonObject json = new JsonObject();
		JsonObject articlesData = new JsonObject();
		articlesData.addProperty("eltárolt cikkek száma", StatisticsActivator.statistics.getStoredArticlesCount());
		articlesData.addProperty("frissített cikkek száma", StatisticsActivator.statistics.getUpdatedArticlesCount());
		articlesData.addProperty("újonnan beillesztett cikkek száma", StatisticsActivator.statistics.getInsertedArticlesCount());
		articlesData.addProperty("nem feldolgozott cikkek száma", StatisticsActivator.statistics.getNotProcessedArticlesCount());
		
		json.add("articlesData", articlesData);
		
		resp.setContentType("application/json");
		resp.getWriter().write(json.toString());
	}

}
