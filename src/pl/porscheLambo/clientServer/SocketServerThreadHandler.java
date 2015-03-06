package pl.porscheLambo.clientServer;

import java.util.HashMap;

public class SocketServerThreadHandler {

	private static HashMap<String, Thread> threads = new HashMap<String, Thread>();
	
	public SocketServerThreadHandler() {
		
	}
	
	public void addThread(String username, Thread thread) {
		threads.put(username, thread);
	}
	
	public void removeThread(String username) {
		threads.remove(username);
	}
	
	public Thread getThreadByUsername(String username) {
		return threads.get(username);
	}

	public static HashMap<String, Thread> getThreads() {
		return threads;
	}

	public static void setThreads(HashMap<String, Thread> threads) {
		SocketServerThreadHandler.threads = threads;
	}

}
