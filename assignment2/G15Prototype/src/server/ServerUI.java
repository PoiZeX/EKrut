package server;

import javafx.application.Application;
import javafx.stage.Stage;
import server.EchoServer;
import utils.ChangeScreen;

import java.io.IOException;
import java.util.Vector;

import controller.ServerConfigurationUIController;

public class ServerUI extends Application {
	public static final int DEFAULT_PORT = 5555;

	static EchoServer EchoServer;

	public static void main(String[] args) throws Exception {
		launch(args);
	}

	public void start(Stage primaryStage) throws Exception {
		ChangeScreen screen = new ChangeScreen();
		screen.changeScreen(primaryStage, "/boundary/ServerConfigurationUI.fxml");
	}

	public static void runServer(String portUI, String DBAddress, String username, String password) {
		int serverPort = 0;
		try {
			serverPort = Integer.parseInt(portUI);
		} catch (Throwable t) {
			System.out.println("ERROR - Could not connect!");
			return;
		}
		
		EchoServer = new EchoServer(serverPort, DBAddress, username, password);
		try {
			EchoServer.listen();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR - Could not listen for clients!");
		}
	}

	public static void disconnect() {
	    if (EchoServer == null) {
	    	EchoServer.stopListening();
	      } else {
	        try {
	        	EchoServer.close();
	        } catch (IOException e) {
	          e.printStackTrace();
	        } 
	      } 
	      System.out.println("Server Disconnected");
	}
}
