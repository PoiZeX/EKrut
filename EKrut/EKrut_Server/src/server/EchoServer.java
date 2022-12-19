
package server;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import common.Message;
import common.MessageType;
import controllerDb.LoginDbController;
import entity.ConnectedClientEntity;
import entity.DatabaseEntity;
import entity.SubscriberEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mysql.MySqlClass;
import ocsf.server.*;

public class EchoServer extends AbstractServer {
	DatabaseEntity DatabaseController;
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
		this.DatabaseController = new DatabaseEntity(username, password, DBAddress);
	}

	@Override
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		if (msg instanceof String) {
			System.out.println("Message received: " + msg + " from " + client);
		} else if (msg instanceof Message) {
			System.out.println("Message received: " + ((Message) msg).getTask().toString() + " from " + client);
			try {
				MessageHandlerServer.HandleMessage((Message) msg, client);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			client.sendToClient("Server done");
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
