
package server;

import entity.DatabaseEntity;
import mysql.MySqlClass;
import ocsf.server.*;

public class EchoServer extends AbstractServer {
	DatabaseEntity DatabaseController;

	public EchoServer(int port, String DBAddress, String username, String password) {
		super(port);
		this.DatabaseController = new DatabaseEntity(username, password, DBAddress);
	}

	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		System.out.println("Message received: " + msg + " from " + client);
		try {
			client.sendToClient(msg);
		}
		catch(Exception ex) {
			System.err.println(ex);
		}
		
	}

	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
		try {
			MySqlClass.connectToDb(this.DatabaseController.getDBAddress(), this.DatabaseController.getUsername(),
					this.DatabaseController.getPassword());
		} catch (Exception ex) {
			System.out.println("Error! DataBase Connection Failed");
		}
	}

	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}
}
