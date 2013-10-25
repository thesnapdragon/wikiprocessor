package wikiprocessor.dump;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import wikiprocessor.parser.ParserActivator;
import wikiprocessor.parser.QueueManager;

/**
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.10.25.
 * 
 * Loading dump in a thread
 */
public class DumpLoader implements Runnable {
	
	// dump file path variable
	private String dumpFilePath = null;
	
	// QueueManager
	private QueueManager dumpQueueManager = null;

	/**
	 * sets dump file path
	 * @param dumpFilePath to set
	 * @param database 
	 */
	public DumpLoader(String dumpFilePath, QueueManager dumpQueueManager) {
		this.dumpFilePath = dumpFilePath;
		this.dumpQueueManager = dumpQueueManager;
	}

	/**
	 * first: counts articles, then read all articles in
	 */
	@Override
	public void run() {
		try {
			XMLReader xr = XMLReaderFactory.createXMLReader();
			FileInputStream fis = new FileInputStream(this.dumpFilePath);
		
			// preprocessing: getting page count
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			DumpPreprocessor preprocessor = new DumpPreprocessor(); 
			xr.setContentHandler(preprocessor);
			xr.setErrorHandler(preprocessor);
			ParserActivator.logger.trace("Dump preprocessing started!");
			xr.parse(new InputSource(isr));
			ParserActivator.logger.trace("Dump preprocessing ended!");
			ParserActivator.statistics.setPageCount(preprocessor.getPages());
			
			// reading the dump
			fis = new FileInputStream(this.dumpFilePath);
			isr = new InputStreamReader(fis, "UTF-8");
			DumpReader dumpReader = new DumpReader(this.dumpQueueManager);
			xr.setContentHandler(dumpReader);
			xr.setErrorHandler(dumpReader);
			xr.parse(new InputSource(isr));
		} catch (SAXException e) {
			ParserActivator.logger.error("Can not parse dump!");
			e.printStackTrace();
		} catch (IOException e) {
			ParserActivator.logger.error("Can not read dumpfile");
			e.printStackTrace();
		}
	}
}
