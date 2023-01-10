// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package client;

import java.io.*;

import Store.DataStore;
import Store.NavigationStoreController;
import client.*;
import common.ChatIF;
import common.CommonFunctions;
import common.PopupTypeEnum;

public class ClientController implements ChatIF {

	public static int DEFAULT_PORT = 5555; // TODO: Extract this and other const do other class

	ChatClient client;

	public ClientController(String host, int port) {
		try {
			client = new ChatClient(host, port, this);
		} catch (Exception exception) {
			CommonFunctions.createPopup(PopupTypeEnum.Error, "Can't setup connection! Terminating client.");
			NavigationStoreController.closeAllScreens();
		}
	}

	/**
	 * This method waits for input from the console. Once it is received, it sends
	 * it to the client's message handler.
	 */
	public boolean accept(String str) {
		return client.handleMessageFromClientUI(str);
	}

	public boolean acceptObj(Object obj) {
		return client.handleMessageFromClient(obj);
	}

	/**
	 * This method overrides the method in the ChatIF interface. It displays a
	 * message onto the screen.
	 *
	 * @param message The string to be displayed.
	 */
	public void display(String message) {
		System.out.println("> " + message);
	}

}
//End of ConsoleChat class
