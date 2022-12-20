package server;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.io.IOException;

import common.Message;
import common.TaskType;

public class ServerUI extends Application {
	public static final int DEFAULT_PORT = 5555;

	static EchoServer EchoServer;
	public static void main(String[] args) throws Exception {
		launch(args);
	}

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
		EchoServer.sendToAllClients(new Message(TaskType.ClientDisconnect, null));
		try {
			Thread.sleep(2000);  // enough time to update the clients table. More flexible way is to use semaphore...
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
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
