
package server;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mysql.cj.MysqlConnection;

import common.MessageType;
import entity.ConnectedClient;
import entity.DatabaseEntity;
import entity.Subscriber;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mysql.MySqlClass;
import ocsf.server.*;

public class EchoServer extends AbstractServer {
	DatabaseEntity DatabaseController;
	private static ObservableList<ConnectedClient> clientList;

	static {
		EchoServer.clientList = FXCollections.observableArrayList();
	}

	public static void setClientList(final ObservableList<ConnectedClient> clientList) {
		EchoServer.clientList = clientList;
	}

	public static ObservableList<ConnectedClient> getClientList() {
		return EchoServer.clientList;
	}

	protected void clientConnected(final ConnectionToClient client) {
	}

	public EchoServer(int port, String DBAddress, String username, String password) {
		super(port);
		this.DatabaseController = new DatabaseEntity(username, password, DBAddress);
	}

	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		if (msg instanceof ArrayList) {
			// i know its ArrayList of subscribers but TODO check this
			ArrayList<Subscriber> subscribersLst = (ArrayList<Subscriber>) msg;
			SubscribersDbController.updateSubscribersEntities(client, subscribersLst);
		} else if (msg instanceof MessageType) {
			MessageType type = (MessageType) msg;
			switch (type) {
			case ClientConnect:
				updateClientList(client, "Connect");
				break;
			case ClientDisconnect:
				updateClientList(client, "Disconnect");
				break;
			case LoadSubscribers:
				SubscribersDbController.getTable(client);
				break;

			default:
				System.out.println("Message received: " + msg + " from " + client);
				break;
			}

		}
		
		System.out.println("Message received: " + msg + " from " + client);
		try {
			client.sendToClient("Server done");
		} catch (Exception ex) {
			System.err.println(ex);
		}

	}

	// Extract it from here later
	static void updateClientList(final ConnectionToClient client, final String connectionStatus) {
		final ObservableList<ConnectedClient> clientList = EchoServer.getClientList();
		for (int i = 0; i < clientList.size(); ++i) {
			if (((ConnectedClient) clientList.get(i)).getIp().equals(client.getInetAddress().getHostAddress())) {
				clientList.remove(i);
			}
		}
		clientList.add((ConnectedClient) new ConnectedClient(client.getInetAddress().getHostAddress(),
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
