
package server;

import java.io.IOException;
import common.Message;
import entity.ConnectedClientEntity;
import entity.DatabaseEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mysql.MySqlClass;
import ocsf.server.*;
import utils.ScheduledTasksController;

/**
 * The Class EchoServer.
 */
public class EchoServer extends AbstractServer {
	DatabaseEntity databaseEntity;
	private static ObservableList<ConnectedClientEntity> clientList;

	static {
		EchoServer.clientList = FXCollections.observableArrayList();
	}

	/**
	 * Sets the client list.
	 *
	 * @param clientList the new client list
	 */
	public static void setClientList(final ObservableList<ConnectedClientEntity> clientList) {
		EchoServer.clientList = clientList;
	}

	/**
	 * Gets the client list.
	 *
	 * @return the client list
	 */
	public static ObservableList<ConnectedClientEntity> getClientList() {
		return EchoServer.clientList;
	}

	/**
	 * Instantiates a new echo server.
	 *
	 * @param port the port
	 * @param DBAddress the DB address
	 * @param username the username
	 * @param password the password
	 */
	public EchoServer(int port, String DBAddress, String username, String password) {
		super(port);
		this.databaseEntity = new DatabaseEntity(username, password, DBAddress);
	}

	/**
	 * Handle message from client.
	 *
	 * @param msg the msg
	 * @param client the client
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
	 * Update client list.
	 *
	 * @param client the client
	 * @param connectionStatus the connection status
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
	 * Server started.
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
		stc.tasksMonthlyExecuter(); 		// run first time to validate we do not miss the first of the month (when crash occured)
		stc.setupTimer(24*60*60* 1000);  	// setup ONE DAY timer 
	}

	/**
	 * Server stopped.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}

	/**
	 * Checks if is all clients disconnected.
	 *
	 * @return true, if is all clients disconnected
	 */
	public boolean isAllClientsDisconnected() {
		for (ConnectedClientEntity client : clientList)
			if (client.getStatus().equals("Connect"))
				return false;
		return true;
	}
}
