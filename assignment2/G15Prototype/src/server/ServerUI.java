package server;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import server.EchoServer;
import utils.ChangeScreen;
import java.io.IOException;
import java.util.Vector;

import controllerGui.ServerConfigurationController;

public class ServerUI extends Application {
	public static final int DEFAULT_PORT = 5555;

	static EchoServer EchoServer;

	public static void main(String[] args) throws Exception {
		launch(args);
	}

	public void start(Stage primaryStage) throws Exception {
		ChangeScreen screen = new ChangeScreen();
		screen.changeScreen(primaryStage, "/boundary/ServerConfigurationBoundary.fxml", null);
		primaryStage.setTitle("Ekrut Server");
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				if(EchoServer!=null) {
				ServerUI.disconnect();  // force disconnect server
				
				}
			}
		});
	
	
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
