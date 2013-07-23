package wikiprocessor.statistics.data;

import wikiprocessor.statistics.data.service.StatisticsDataService;

/**
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.07.09.
 *
 * Handling statistics datas
 */
public class StatisticsData implements StatisticsDataService {
	
	private int updatedArticlesCount;
	private int insertedArticlesCount;
	private int notProcessedArticlesCount;
	private int errorsCount;
	
	public StatisticsData() {
		this.updatedArticlesCount = 0;
		this.insertedArticlesCount = 0;
		this.notProcessedArticlesCount = 0;
		this.errorsCount = 0;
	}

	@Override
	public int getStoredArticlesCount() {
		return getUpdatedArticlesCount() + getInsertedArticlesCount();
	}
	
	@Override
	public int getUpdatedArticlesCount() {
		return updatedArticlesCount;
	}

	@Override
	public void increaseUpdatedArticlesCount() {
		this.updatedArticlesCount++;
	}
	
	@Override
	public int getInsertedArticlesCount() {
		return insertedArticlesCount;
	}

	@Override
	public void increaseInsertedArticlesCount() {
		this.insertedArticlesCount++;
	}

	@Override
	public int getNotProcessedArticlesCount() {
		return notProcessedArticlesCount;
	}

	@Override
	public void increaseNotProcessedArticlesCount() {
		this.notProcessedArticlesCount++;
	}

	@Override
	public int getErrorsCount() {
		return errorsCount;
	}

	@Override
	public void increaseErrorsCount() {
		this.errorsCount++;
	}

	@Override
	public int getQueueLength() {
		return 0;
	}

}
