// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.ChatIF;
import common.Message;
import common.MessageType;
import controllerGui.LoginController;

import java.io.*;

import Store.NavigationStoreController;
import entity.SubscriberEntity;
import entity.UserEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ChatClient extends AbstractClient {

	ChatIF clientUI;
	// public static ObservableList<SubscriberEntity> subscribers;
	public static boolean awaitResponse = false;
	public static ObservableList<SubscriberEntity> subscribers;

	public ChatClient(String host, int port, ChatIF clientUI) throws IOException {
		super(host, port); // Call the superclass constructor
		this.clientUI = clientUI;
		// openConnection();
	}

	public void handleMessageFromServer(Object msg) {
		awaitResponse = false;
		Message msgFromServer = (Message) msg;
		MessageType task = msgFromServer.getTask();
		Object obj = msgFromServer.getObject();
		// ---- Messages ---- //
		switch (task) {
		case ServerDisconnect:
			try {
				sendToServer((Object) MessageType.ClientDisconnect);
			} catch (IOException e) {
				e.printStackTrace();
			}
			NavigationStoreController.closeAllScreens(); // force closing since server is disconnected
			break;
		// ---- Login
		case UserFromServerDB:
			LoginController.validUserFromServer((UserEntity) obj);
		// ---- Delivery
		default:
			break;
		}

	}

	public void handleMessageFromClientUI(String message) {
		try {
			openConnection();// in order to send more than one message
			awaitResponse = true;
			sendToServer(message);
			// wait for response
			while (awaitResponse) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			clientUI.display("Could not send message to server: Terminating client." + e);
			quit();
		}
	}

	public void handleMessageFromClient(Message message) {
		try {
			openConnection();// in order to send more than one message
			awaitResponse = true;
			sendToServer(message);
			// wait for response
			while (awaitResponse) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			clientUI.display("Could not send message to server: Terminating client." + e);
			quit();
		}

	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}

//	static {
//		ChatClient.subscribers = FXCollections.observableArrayList();
//	}
//
//	public static void setClientList(final ObservableList<SubscriberEntity> subscribers) {
//		ChatClient.subscribers = subscribers;
//	}
//
//	public static ObservableList<SubscriberEntity> getClientList() {
//		return ChatClient.subscribers;
//	}

}
