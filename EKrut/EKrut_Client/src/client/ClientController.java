
package client;

import Store.NavigationStoreController;
import enums.PopupTypeEnum;
import interfaces.ChatIF;
import utils.PopupSetter;
/**
 * Class that implements the ChatIF ocsf interface, handles the connection with server
 *
 */
public class ClientController implements ChatIF {

	public static int DEFAULT_PORT = 5555; // Extract this and other const do other class

	ChatClient client;

	/**
	 * Client controller to define chat between server and client
	 * @param host ip
	 * @param port 4 digits port
	 */
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
	
	/**
	 * Get object from server
	 * @param obj general message
	 * @return true/false
	 */
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
