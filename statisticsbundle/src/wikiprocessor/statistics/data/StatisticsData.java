package wikiprocessor.statistics.data;

import java.util.HashMap;

import wikiprocessor.statistics.data.service.StatisticsDataService;

/**
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.10.25.
 *
 * Handling statistics datas
 */
public class StatisticsData implements StatisticsDataService {
	
	private int updatedArticlesCount;
	private int insertedArticlesCount;
	private int notProcessedArticlesCount;
	private double chainSpeed;
	
	private int queueLength;
	
	private int errorLogCount;
	private int warningLogCount;
	private int traceLogCount;
	private int debugLogCount;
	
	private HashMap<String, Double> workingParserRatios;
	
	private int pageCount;
	private int processedPages;
	
	public StatisticsData() {
		this.updatedArticlesCount = 0;
		this.insertedArticlesCount = 0;
		this.notProcessedArticlesCount = 0;
		this.chainSpeed = 0;

		this.queueLength = 0;
		
		this.errorLogCount = 0;
		this.warningLogCount = 0;
		this.traceLogCount = 0;
		this.debugLogCount = 0;
		
		this.workingParserRatios = new HashMap<String, Double>();
		
		this.pageCount = 0;
		this.processedPages = 0;
		
		Thread speedCounter = new Thread() {
			private int lastStoredArticlesCount = 0; 
			public void run() {
				while (true) {
				this.lastStoredArticlesCount = getStoredArticlesCount();
					try {
						Thread.sleep(1000 * 60);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					setChainSpeed(getStoredArticlesCount() - lastStoredArticlesCount);
				}
			}
		};
		speedCounter.start();
	}

	@Override
	public double getWorkingParserRatio(String name) {
		if (!(workingParserRatios.containsKey(name))) {
			return 0;
		} else {
			return workingParserRatios.get(name);
		}
	}

	@Override
	public void setWorkingParserRatio(String name, double workingParserRatio) {
		this.workingParserRatios.put(name, workingParserRatio);
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

	@Override
	public double getProcessStatus() {
		return ((double)this.processedPages / (double)this.pageCount) * 100.0;
	}

	@Override
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	@Override
	public void increaseProcessedPages() {
		this.processedPages++;
	}

	@Override
	public double getChainSpeed() {
		return this.chainSpeed;
	}
	
	public void setChainSpeed(double speed) {
		this.chainSpeed = speed;
	}

}
