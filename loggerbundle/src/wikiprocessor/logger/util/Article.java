package wikiprocessor.logger.util;

/**
 * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
 * @version 1.0
 * @since 2013.07.17.
 * 
 * Wikipedia Article class
 */
public class Article {
	private String title;
	private String text;
	private int revision;
	
	public Article(String title, int revision) {
		this.title = title;
		this.revision = revision;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getRevision() {
		return revision;
	}
	public void setRevision(int revision) {
		this.revision = revision;
	}
}
