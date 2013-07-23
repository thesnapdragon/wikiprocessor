package wikiprocessor.statistics.data.service;

/**
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.07.23.
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
	
	public int getErrorsCount();

	public void increaseErrorsCount();
	
	public int getQueueLength();
}
