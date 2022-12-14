
package server;

import java.io.IOException;
import common.Message;
import entity.ConnectedClientEntity;
import entity.DatabaseEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mysql.MySqlClass;
import ocsf.server.*;

public class EchoServer extends AbstractServer {
	DatabaseEntity databaseEntity;
	private static ObservableList<ConnectedClientEntity> clientList;

	static {
		EchoServer.clientList = FXCollections.observableArrayList();
	}

	public static void setClientList(final ObservableList<ConnectedClientEntity> clientList) {
		EchoServer.clientList = clientList;
	}

	public static ObservableList<ConnectedClientEntity> getClientList() {
		return EchoServer.clientList;
	}

	protected void clientConnected(final ConnectionToClient client) {
	}

	public EchoServer(int port, String DBAddress, String username, String password) {
		super(port);
		this.databaseEntity = new DatabaseEntity(username, password, DBAddress);
	}

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
			client.sendToClient(true);
		} catch (Exception ex) {
			System.err.println(ex);
		}
	}

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

	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
		try {
			MySqlClass.connectToDb(this.databaseEntity.getDBAddress(), this.databaseEntity.getUsername(),
					this.databaseEntity.getPassword());
		} catch (Exception ex) {
			System.out.println("Error! DataBase Connection Failed");
		}
	}

	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}
}
