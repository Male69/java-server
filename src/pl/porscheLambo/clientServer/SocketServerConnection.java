package pl.porscheLambo.clientServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class SocketServerConnection implements Runnable {
	
	private final static Logger log = Logger.getLogger(SocketServerConnection.class.getName()); 
	private Socket connection;
	private BufferedReader userMsg; 
	private BufferedWriter serverMsg;
	private String username;
	private String message;
	private List<String> connections;
	private String firstPartMsg;
	private String secondPartMsg;
	private String sender;
	
	public SocketServerConnection(Socket connection, String username) {
		this.connection = connection;
		this.username = username;
	}

	
	@Override
	public void run() {		
		while(true) {
			readRequest();
			sendResponse();
		}
	}
	
	public String readRequest() {
		try {
			userMsg = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			if(userMsg != null) {
				message = userMsg.readLine();
				log.info(message);
				splitter(message);
				if(firstPartMsg.equals("connections")) {
					connections  = new ArrayList<String>();
					connections = Singleton.getInstance().getAllUsernames();
					message = sendConnections(connections);
					
				}else {
					username = firstPartMsg;
					message = Singleton.getInstance().getUsernameBySocket(connection) + ":" + secondPartMsg;
				}
				//log.info("Message from the " + username+ " with local address " + connection.getLocalAddress() + ": " + message);
			}
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		return message;
	}
	
	public void splitter(String msg) {
		String[] messageParts = message.split(":");
		firstPartMsg = messageParts[0];
		if(messageParts[0].equals("connections")) {
			
		} else {
			secondPartMsg = messageParts[1];
		}
		
		log.info("First part of the msg" + firstPartMsg);
	}
	
	public void sendResponse() {
		//String[] messageParts = message.split(":");
		//username = messageParts[0];
		//message = messageParts[1];
		//log.info("Username: " + username + "Message: " + message);
		if(message != null) {
			log.info(message);
				try {
					serverMsg = new BufferedWriter(new OutputStreamWriter(Singleton.getInstance().getUserSocket(username).getOutputStream()));
					//serverMsg.write(Singleton.getInstance().getUsernameBySocket(connection) + ":" + message);
					serverMsg.write(message);
					serverMsg.newLine();
					serverMsg.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	public String sendConnections(List<String> connections) {
		String result = null;
		result = "connections:" + String.join(":", connections);
		log.info(result);
		return result;
		
	}
	
}
