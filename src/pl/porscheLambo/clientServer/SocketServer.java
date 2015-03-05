package pl.porscheLambo.clientServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class SocketServer {

	private final static Logger log = Logger.getLogger(SocketServer.class.getName());
	private static final int port = 9992;

	private Socket connection;
	private ServerSocket serverSocket;
	private String username;
	
	public void launch(int port) {
		try {
			serverSocket = new ServerSocket(port);

			log.info("Waiting for clients...");
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public void connectClients() {
		try {
			while(true) {
				connection = serverSocket.accept();
				username = readLogin();
				Singleton.getInstance().addConnection(username, connection);
				log.info("User: "  + username + "has logged in");
				
				new Thread(new SocketServerConnection(connection, username)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public String readLogin() {
		String userLogin = null;

		try {
			BufferedReader userMsg = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			userLogin = userMsg.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return userLogin;
	}
	
	public static void main(String[] args) {
		SocketServer socketServer = new SocketServer();

		socketServer.launch(port);
		socketServer.connectClients();
	}
}
