package shared.console;

public class ConsoleLine {
	public ContentCategory category;
	public String content;
	public String filter;
	
	public ConsoleLine(ContentCategory category, String content, String filter) {
		this.category = category;
		this.content = content;
		this.filter = filter;
	}
	
}
