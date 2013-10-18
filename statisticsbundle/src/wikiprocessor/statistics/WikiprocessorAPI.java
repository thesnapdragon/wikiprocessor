package wikiprocessor.statistics;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		
		// statistic datas about processing articles
		JsonObject articlesData = new JsonObject();
		articlesData.addProperty("eltárolt cikkek száma", StatisticsActivator.statistics.getStoredArticlesCount());
		articlesData.addProperty("frissített cikkek száma", StatisticsActivator.statistics.getUpdatedArticlesCount());
		articlesData.addProperty("újonnan beillesztett cikkek száma", StatisticsActivator.statistics.getInsertedArticlesCount());
		articlesData.addProperty("nem feldolgozott cikkek száma", StatisticsActivator.statistics.getNotProcessedArticlesCount());
		
		json.add("articlesData", articlesData);
		
		// statistic datas about log messages
		JsonObject logsData = new JsonObject();
		logsData.addProperty("error üzenetek száma", StatisticsActivator.statistics.getErrorLogCount());
		logsData.addProperty("warning üzenetek száma", StatisticsActivator.statistics.getWarningLogCount());
		//logsData.addProperty("trace üzenetek száma", StatisticsActivator.statistics.getTraceLogCount());
		logsData.addProperty("debug üzenetek száma", StatisticsActivator.statistics.getDebugLogCount());
		
		json.add("logsData", logsData);
		
		// statistic data about queuelength
		json.addProperty("queuelength", StatisticsActivator.statistics.getQueueLength());
		
		// statistic data about working parsoid parsers count
		json.addProperty("workingparsoids", StatisticsActivator.statistics.getWorkingParserRatio());
		
		resp.setContentType("application/json");
		resp.getWriter().write(json.toString());
	}

}
