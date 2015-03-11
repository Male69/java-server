package pl.porscheLambo.clientServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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
	private boolean stopThread;
	
	public SocketServerConnection(Socket connection, String username) {
		this.connection = connection;
		this.username = username;
	}

	@Override
	public void run() {
		stopThread = false;
		sendAllConnections();
		while(stopThread == false) {
				readRequest();
				sendResponse();			
		}
		try {
			Singleton.getInstance().getUserSocket(username).close();	
			Singleton.getInstance().removeConnection(username);
			SocketServerThreadHandler.getThreads().get(username).join(4000);
			SocketServerThreadHandler.getThreads().remove(username);
		} catch (IOException | InterruptedException  e) {
			e.printStackTrace();
		}
		log.info("Connection with " + username + " is closed");
		Singleton.getInstance().getConnections().remove(username);
		sendAllConnections();
	}
	
	public String readRequest() {
		try {
			userMsg = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			if(userMsg != null) {
				message = userMsg.readLine();
				log.info(message);
				splitter(message);
				if(secondPartMsg.equals("exit")) {
					log.info("wchodze w warunek exita");
					username = firstPartMsg;
					message = "Connection is closed";
					
				} else {
					username = firstPartMsg;
					message = Singleton.getInstance().getUsernameBySocket(connection) + ":" + secondPartMsg;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return message;
	}
	
	public void splitter(String msg) {
		String[] messageParts = message.split(":");

		firstPartMsg = messageParts[0];
		if(!messageParts[0].equals("connections") ) {
			secondPartMsg = messageParts[1];
		}
		log.info("First part of the msg" + firstPartMsg);
	}
	
	public void sendResponse() {
		if(message != null) {
				try {
					log.info("KONCZE POLACZENIE");
					serverMsg = new BufferedWriter(new OutputStreamWriter(Singleton.getInstance().getUserSocket(username).getOutputStream()));
					serverMsg.write(message);
					serverMsg.newLine();
					serverMsg.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			if(message.equals("Connection is closed")) {
				stopThread = true;			
			}
		}
	}
	
	public String sendConnections(List<String> connections) {
		String result = "connections:" + String.join(":", connections);
		log.info(result);
		
		return result;
	}
	
	 public void sendAllConnections() {
         String message = null;
         if(Singleton.getInstance().getConnections().size() != 0 ) {
             for (String string : Singleton.getInstance().getAllUsernames()) {
                 message += string + ":";
             }
             StringBuilder stringBuilder = new StringBuilder(message);
             message = stringBuilder.delete(message.lastIndexOf(message), 4).toString();
             message = stringBuilder.deleteCharAt(message.length()-1).toString();
             for (Socket socket : Singleton.getInstance().getAllSockets() ) {
                 try {
                	 BufferedWriter serverMsg = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                     serverMsg.write("connections:" + message);
                     serverMsg.newLine();
                     serverMsg.flush();
                 } catch (IOException e) {
                         e.printStackTrace();
                 }
             }
         }
	 }
}
