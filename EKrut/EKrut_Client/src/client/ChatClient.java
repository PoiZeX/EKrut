// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;
import ocsf.client.*;
import common.ChatIF;
import common.Message;
import java.io.*;

import Store.NavigationStoreController;
import entity.SubscriberEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ChatClient extends AbstractClient {

	ChatIF clientUI;
	public static boolean awaitResponse = false;


	public ChatClient(String host, int port, ChatIF clientUI) throws IOException {
		super(host, port); // Call the superclass constructor
		this.clientUI = clientUI;
		// openConnection();
	}


	public void handleMessageFromServer(Object msg) {
		awaitResponse = false;
		if (msg instanceof String) {
			System.out.println("Message received: " + msg);
		} else if (msg instanceof Message) {
			try {
				MessageHandler.Handle(this, (Message) msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean handleMessageFromClientUI(String message) {
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
//			e.printStackTrace();
//			clientUI.display("Could not send message to server: Terminating client." + e);
//			quit();
			return false;
		}
		return true;
	}


	public boolean handleMessageFromClient(Object message) {
		try {
			openConnection(); // in order to send more than one message
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
			//clientUI.display("Could not send message to server: Terminating client." + e);
			//quit();
			return false;
		}
		return true;
	}


	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
		NavigationStoreController.closeAllScreens();
	}

	
}
