package wikiprocessor.statistics.data;

import wikiprocessor.statistics.data.service.StatisticsDataService;

/**
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.10.18.
 *
 * Handling statistics datas
 */
public class StatisticsData implements StatisticsDataService {
	
	private int updatedArticlesCount;
	private int insertedArticlesCount;
	private int notProcessedArticlesCount;
	
	private int queueLength;
	
	private int errorLogCount;
	private int warningLogCount;
	private int traceLogCount;
	private int debugLogCount;
	
	private double workingParserRatio;
	
	public StatisticsData() {
		this.updatedArticlesCount = 0;
		this.insertedArticlesCount = 0;
		this.notProcessedArticlesCount = 0;

		this.queueLength = 0;
		
		this.errorLogCount = 0;
		this.warningLogCount = 0;
		this.traceLogCount = 0;
		this.debugLogCount = 0;
		
		this.workingParserRatio = 0;
	}

	@Override
	public double getWorkingParserRatio() {
		return workingParserRatio;
	}

	@Override
	public void setWorkingParserRatio(double workingParserRatio) {
		this.workingParserRatio = workingParserRatio;
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
	public int getQueueLength() {
		return queueLength;
	}

	@Override
	public void increaseQueueLength() {
		this.queueLength++;
	}
	
	@Override
	public void decreaseQueueLength() {
		this.queueLength--;
	}

	@Override
	public int getErrorLogCount() {
		return errorLogCount;
	}

	@Override
	public void increaseErrorLogCount() {
		this.errorLogCount++;
	}
	
	@Override
	public int getWarningLogCount() {
		return warningLogCount;
	}

	@Override
	public void increaseWarningLogCount() {
		this.warningLogCount++;
	}
	
	@Override
	public int getTraceLogCount() {
		return traceLogCount;
	}

	@Override
	public void increaseTraceLogCount() {
		this.traceLogCount++;
	}
	
	@Override
	public int getDebugLogCount() {
		return debugLogCount;
	}

	@Override
	public void increaseDebugLogCount() {
		this.debugLogCount++;
	}

}
