
package client;

import Store.NavigationStoreController;
import enums.PopupTypeEnum;
import interfaces.ChatIF;
import utils.PopupSetter;

public class ClientController implements ChatIF {

	public static int DEFAULT_PORT = 5555; // Extract this and other const do other class

	ChatClient client;

	public ClientController(String host, int port) {
		try {
			client = new ChatClient(host, port, this);
		} catch (Exception exception) {
			PopupSetter.createPopup(PopupTypeEnum.Error, "Can't setup connection! Terminating client.");
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
