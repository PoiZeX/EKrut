// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import server.EchoServer;
import client.*;
import common.ChatIF;
import java.io.*;
import java.util.ArrayList;

import entity.ConnectedClient;
import entity.Subscriber;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ChatClient extends AbstractClient {

	ChatIF clientUI;
	public static ObservableList<Subscriber> subscribers;
	public static boolean awaitResponse = false;


	public ChatClient(String host, int port, ChatIF clientUI) throws IOException {
		super(host, port); // Call the superclass constructor
		this.clientUI = clientUI;
		// openConnection();
	}


	public void handleMessageFromServer(Object msg) {
		System.out.println(msg);
		awaitResponse = false;
		if(msg instanceof Subscriber) {
			subscribers.add((Subscriber)msg);
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


	public void handleMessageFromClient(Object message) {
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
	
	
	static {
		ChatClient.subscribers = FXCollections.observableArrayList();
	}

	public static void setClientList(final ObservableList<Subscriber> subscribers) {
		ChatClient.subscribers = subscribers;
	}

	public static ObservableList<Subscriber> getClientList() {
		return ChatClient.subscribers;
	}
	
}
