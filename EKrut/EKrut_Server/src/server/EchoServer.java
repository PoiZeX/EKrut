
package server;

import java.io.IOException;
import common.Message;
import common.ScheduledTasksController;
import entity.ConnectedClientEntity;
import entity.DatabaseEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mysql.MySqlClass;
import ocsf.server.*;

/**
 * The EchoServer class is an implementation of the OCSF library's
 * AbstractServer class. It is a basic echo server that listens for incoming
 * connections from clients and handles the communication between them. The
 * server has a port number which it listens on, a DatabaseEntity that contains
 * the connection details to the database, and an ObservableList of
 * ConnectedClientEntity that stores the clients that are currently connected to
 * the server. The server listens for incoming connections and handles incoming
 * messages. If the message is a string, it will print it to the console. If the
 * message is an instance of the Message class, it will pass it to the
 * MessageHandler.handle() method for further processing. The server also has a
 * start and stop methods, serverStarted() and serverStopped(), which are called
 * by the OCSF library when the server starts and stops. They can be overridden
 * to provide custom functionality, but in this case, it just prints a message
 * to the console indicating that the server has started or stopped.
 */
public class EchoServer extends AbstractServer {
	DatabaseEntity databaseEntity;
	private static ObservableList<ConnectedClientEntity> clientList;

	static {
		EchoServer.clientList = FXCollections.observableArrayList();
	}

	/**
	 * Sets the clients list
	 * 
	 * @param clientList
	 */
	public static void setClientList(final ObservableList<ConnectedClientEntity> clientList) {
		EchoServer.clientList = clientList;
	}

	public static ObservableList<ConnectedClientEntity> getClientList() {
		return EchoServer.clientList;
	}

	protected void clientConnected(final ConnectionToClient client) {
	}

	/**
	 * Creates an instance of EchoServer and initializes the port and the database
	 * credentials for connection
	 * 
	 * @param port      the port number on which the server listens for connections
	 * @param DBAddress the address of the database
	 * @param username  the username used for the database
	 * @param password  the password used for the database
	 */
	public EchoServer(int port, String DBAddress, String username, String password) {
		super(port);
		this.databaseEntity = new DatabaseEntity(username, password, DBAddress);
	}

	/**
	 * Handles message received from a connected client
	 * 
	 * @param msg    the message received
	 * @param client the client that sent the message
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		if (msg instanceof String) {
			System.out.println("Message received: " + msg + " from " + client);
		} else if (msg instanceof Message) {
			try {
				MessageHandler.Handle((Message) msg, client);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			client.sendToClient(true); // in case no one sent a msg to client
		} catch (Exception ex) {
			System.err.println(ex);
		}

	}

	/**
	 * Update the list of connected clients
	 * 
	 * @param client           the client to be added or removed from the list
	 * @param connectionStatus the status of the client's connection
	 *                         (Connected/Disconnected)
	 */
	// Extract it from here later
	static void updateClientList(final ConnectionToClient client, final String connectionStatus) {
		final ObservableList<ConnectedClientEntity> clientList = EchoServer.getClientList();
		for (int i = 0; i < clientList.size(); ++i) {
			if (((ConnectedClientEntity) clientList.get(i)).getIp().equals(client.getInetAddress().getHostAddress())) {
				clientList.remove(i);
			}
		}
		clientList.add((ConnectedClientEntity) new ConnectedClientEntity(client.getInetAddress().getHostAddress(),
				client.getInetAddress().getHostName(), connectionStatus));
		EchoServer.setClientList(clientList);
	}

	/**
	 * This method is called when the server has started and is ready to accept
	 * connections
	 */
	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
		try {
			MySqlClass.connectToDb(this.databaseEntity.getDBAddress(), this.databaseEntity.getUsername(),
					this.databaseEntity.getPassword());
		} catch (Exception ex) {
			System.out.println("Error! DataBase Connection Failed");
			return;
		}
		ScheduledTasksController stc = new ScheduledTasksController();
		stc.tasksMonthlyExecuter(); // run first time to validate we do not miss the first of the month (when crash occured)
		stc.setupTimer(5* 1000);  // setup ONE DAY timer 24 * 60 * 60 * 1000
	}

	/**
	 * This method is called when the server has been stopped and is no longer
	 * accepting connections
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}

	/**
	 * Return true if all clients disconnected
	 * 
	 * @return
	 */
	public boolean isAllClientsDisconnected() {
		for (ConnectedClientEntity client : clientList)
			if (client.getStatus().equals("Connect"))
				return false;
		return true;
	}
}
