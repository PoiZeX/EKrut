// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package client;

import java.io.*;
import client.*;
import common.ChatIF;
import common.Message;


public class ClientController implements ChatIF {

	public static int DEFAULT_PORT = 5555; // TODO: Extract this and other const do other class


	ChatClient client;

	
	public ClientController(String host, int port) {
		try {
			client = new ChatClient(host, port, this);
		} catch (IOException exception) {
			System.out.println("Error: Can't setup connection!" + " Terminating client.");
			System.exit(1);
		}
	}

	/**
	 * This method waits for input from the console. Once it is received, it sends
	 * it to the client's message handler.
	 */
	public void accept(String str) {
		client.handleMessageFromClientUI(str);

	}

	public void acceptObj(Message msg) {
		client.handleMessageFromClient(msg);
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
