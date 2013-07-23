package wikiprocessor.logger.service;

/**
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.07.09.
 *
 * Interface for Logger class. It wraps the original Apache Log4j class.
 */
public interface LoggerService {
	
	/**
	 * Designates finer-grained informational events than the DEBUG.
	 * @param msg log message
	 */
	public void trace(String msg);
    
	/**
	 * Designates fine-grained informational events that are most useful to debug an application.
	 * @param msg log message
	 */
	public void debug(String msg);
    
    /**
     * Designates informational messages that highlight the progress of the application at coarse-grained level.
     * @param msg log message
     */
    public void info(String msg);
    
    /**
     * Designates potentially harmful situations.
     * @param msg log message
     */
    public void warn(String msg);
    
    /**
     * Designates error events that might still allow the application to continue running.
     * @param msg log message
     */
    public void error(String msg);
    
    /**
     * Designates very severe error events that will presumably lead the application to abort.
     * @param msg log message
     */
    public void fatal(String msg);
}
