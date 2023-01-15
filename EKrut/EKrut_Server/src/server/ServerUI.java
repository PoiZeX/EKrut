package server;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import mysql.MySqlClass;

import java.io.IOException;
import java.net.BindException;

import common.CommonFunctions;
import common.Message;
import common.TaskType;

public class ServerUI extends Application {
	public static final int DEFAULT_PORT = 5555;

	static EchoServer EchoServer;

	public static void main(String[] args) throws Exception {
		launch(args);
	}

	/**
	 * Start of the server GUI
	 */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/boundary/ServerConfigurationBoundary.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Ekrut Server");
		primaryStage.getIcons().add(new Image("/styles/icons/logotaskbar.png"));
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				if (EchoServer != null) {
					ServerUI.disconnect(); // force disconnect server
				}
			}
		});
		primaryStage.show();
	}

	/**
	 * run the server with mysql connection string
	 * @param portUI
	 * @param DBAddress
	 * @param username
	 * @param password
	 */
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
		} catch (BindException b) {
			System.out.println("ERROR - Could not listen for clients!");
			System.out.println("Port is already in use, please try another port!");
			MySqlClass.isConnectionSuccess = false;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR - Could not listen for clients!");
		}
		
		EchoServer.getClientList().clear();
	}

	/**
	 * Handle server disconnect 
	 */
	public static void disconnect() {
		EchoServer.sendToAllClients(new Message(TaskType.ServerDisconnect));
		int attempts = 5; // waiting maximum 2.5 sec (for not getting stuck in loop)
		try {
			while(!EchoServer.isAllClientsDisconnected() && attempts > 0) {
				Thread.sleep(500); 
				attempts--;
				}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
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
