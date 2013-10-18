package wikiprocessor.statistics.data.service;

/**
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.07.29.
 *
 * Interface for Statistics class.
 */
public interface StatisticsDataService {
	
	public int getStoredArticlesCount();
	
	public int getUpdatedArticlesCount();

	public void increaseUpdatedArticlesCount();

	public int getInsertedArticlesCount();

	public void increaseInsertedArticlesCount();
	
	public int getNotProcessedArticlesCount();

	public void increaseNotProcessedArticlesCount();
	
	public int getQueueLength();
	
	public void increaseQueueLength();
	
	public void decreaseQueueLength();
	
	public int getErrorLogCount();

	public void increaseErrorLogCount();
	
	public int getWarningLogCount();

	public void increaseWarningLogCount();
	
	public int getTraceLogCount();

	public void increaseTraceLogCount();
	
	public int getDebugLogCount();

	public void increaseDebugLogCount();
	
	public double getWorkingParserRatio();
	
	public void setWorkingParserRatio(double workingParserRatio);
}
