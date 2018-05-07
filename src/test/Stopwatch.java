package test;

public class Stopwatch {
	private boolean isRunning;
	private long startTime;
	private String name;
	
	public Stopwatch(String name) {
		this.name = name;
		startTime = 0;
		isRunning = false;
	}
	
	public Stopwatch() {
		this("");
	}
	
	//Methods
	public void start() {
		if(!isRunning) {
			startTime = System.currentTimeMillis();
			isRunning = true;
		}
	}
	
	public long stop() {
		long elapsedTime = getElapsedTime();
		isRunning = false;
		return elapsedTime;
	}
	
	public long reset() {
		long elapsedTime = stop();
		start();
		return elapsedTime;
	}
	
	public long getElapsedTime() {
		if(isRunning)
			return System.currentTimeMillis() - startTime;
		return -1;
	}
	
	@Override
	public String toString() {
		if(isRunning) {
			return "Stopwatch " + name + " ElapsedTime: " + getElapsedTime() + "ms";
		}else {
			return "Stopwatch " + name + " is stopped";
		}
	}
	
	public void print() {
		print(null);
	}
	
	public void print(String comment) {
		if(comment == null)
			comment = "";
		System.out.println(toString() + " " + comment);
	}
	
	
	public void printAndReset() {
		printAndReset(null);
	}
	
	public void printAndReset(String comment) {
		print(comment);
		reset();
	}
	

}
