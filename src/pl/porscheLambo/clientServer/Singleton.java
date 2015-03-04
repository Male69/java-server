package pl.porscheLambo.clientServer;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

class Singleton {
	
	private HashMap<String, Socket> connections = new HashMap<String , Socket>();
	private static final Singleton instance = new Singleton();  
	
	public static Singleton getInstance() {
		return instance;
	}
	
	public void addConnection(String username, Socket socket) {
		connections.put(username, socket);
	}
	
	public Socket getUserSocket(String username) {
		return connections.get(username);
	}
	
	public String getUsernameBySocket(Socket socket) {
		String username = null;

		for (Entry<String, Socket> elem : connections.entrySet()) {
			if(elem.getValue().equals(socket)) {
				username = elem.getKey();
				break;
			}
		}

		return username;
	}
	public List<Socket> getAllSockets() {
		List<Socket> temp = new ArrayList<Socket>();

		for (Socket elem : connections.values()) {
			temp.add(elem);
		}

		return temp;
	}
	
	public List<String> getAllUsernames() {
		List<String> result = new ArrayList<String>();

		for (Entry<String, Socket> elem : connections.entrySet()) {
			result.add(elem.getKey()); 
		}
		
		return result;
	}
}
