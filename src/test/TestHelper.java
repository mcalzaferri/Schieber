package test;

import java.util.ArrayList;

public final class TestHelper {
	private static Stopwatch watch = new Stopwatch("TestHelperWatch");
	private static ArrayList<Thread> registeredThreads = new ArrayList<>();
	
	private TestHelper() {};

	public static void start() {
		registerThread();
		watch.start();
	}
	
	public static void printElapsedTime(String comment) {
		if(registeredThreads.contains(Thread.currentThread())) {
			watch.print(comment);
		}
	}
	public static void printElapsedTime() {
		printAndResetElapsedTime(null);
	}
	
	public static void printAndResetElapsedTime(String comment) {
		if(registeredThreads.contains(Thread.currentThread())) {
			watch.printAndReset(comment);
			System.out.println("Reset TestHelperWatch");
		}
	}
	
	public static void printAndResetElapsedTime() {
		printAndResetElapsedTime(null);
	}
	
	public static void registerThread() {
		Thread t = Thread.currentThread();
		if(!registeredThreads.contains(t))
			registeredThreads.add(t);
	}
}
