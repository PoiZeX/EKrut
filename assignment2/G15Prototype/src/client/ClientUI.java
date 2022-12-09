package client;

import javafx.application.Application;

import javafx.stage.Stage;
import server.EchoServer;
import utils.ChangeScreen;

import java.util.Vector;
import client.ClientController;
import controllerGui.HostClientController;

public class ClientUI extends Application {


	public static void main(String[] args) throws Exception {
		Runtime.getRuntime().addShutdownHook(new Thread(()->{
		    System.out.println("closing...");
		}));
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		//ChangeScreen screen = new ChangeScreen();
		//screen.changeScreen(primaryStage, "/boundary/HostClientUI.fxml");
		HostClientController aFrame = new HostClientController();
		
		aFrame.start(primaryStage);
		
	}
}
