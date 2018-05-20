package shared.console;

import java.util.Vector;

public final class AdvancedConsole {
	
	//Fields
	private static int capacity = 2000;
	private static RingList<ConsoleLine> data = new RingList<>(capacity);
	private static Vector<String> filters = new Vector<>();
	private static boolean redirectToConsole = true;
	private static AdvancedConsoleGui gui = new AdvancedConsoleGui();
	
	//Constructors (Static class -> no constructor)
	private AdvancedConsole() {}
	
	//Methods
	private static void print(ContentCategory category, String text) {
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		String filter = stack[3].toString();
		ConsoleLine content = new ConsoleLine(category, text, filter);

		if(!filters.contains(filter)) {
			filters.add(filter);
			gui.addFilter(category, filter);
		}
		data.add(content);
		
		printToSystemConsole(content);
		printToAdvancedConsole(content);
	}
	
	private static void printToSystemConsole(ConsoleLine content) {
		if(redirectToConsole) {
			switch(content.category) {
			case ERROR:
				System.err.println(content.category + ": " + content.content);
				break;
			default:
				System.out.println(content.category + ": " + content.content);
			}
		}
	}
	
	private static void printToAdvancedConsole(ConsoleLine content) {
		gui.appendLine(content);
	}
	
	public static void printDebug(String content) {
		print(ContentCategory.DEBUG, content);
	}
	
	public static void printInfo(String content) {
		print(ContentCategory.INFO, content);
	}
	
	public static void printError(String content) {
		print(ContentCategory.ERROR, content);
	}

	public static void setRedirectToConsole(boolean redirectToConsole) {
		AdvancedConsole.redirectToConsole = redirectToConsole;
	}
	
	public static RingList<ConsoleLine> getData(){
		return data;
	}
	
	public static void show() {
		gui.show();
	}
	
	public static void hide() {
		gui.hide();
	}
}
