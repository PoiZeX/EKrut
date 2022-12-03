package client;

import javafx.application.Application;

import javafx.stage.Stage;
import server.EchoServer;
import utils.ChangeScreen;

import java.util.Vector;
import client.ClientController;
import controller.HostClientUIController;

public class ClientUI extends Application {


	public static void main(String[] args) throws Exception {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		//ChangeScreen screen = new ChangeScreen();
		//screen.changeScreen(primaryStage, "/boundary/HostClientUI.fxml");
		HostClientUIController aFrame = new HostClientUIController();
		
		aFrame.start(primaryStage);
		
	}
}
