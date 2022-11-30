package client;

import javafx.application.Application;

import javafx.stage.Stage;
import server.EKrutServer;
import utils.ChangeScreen;

import java.util.Vector;
import client.EKrutClient;

public class EKrutClientUI extends Application {

	public static final int DEFAULT_PORT = 5555;

	static EKrutServer EKrutServer;

	public static void main(String[] args) throws Exception {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		ChangeScreen screen = new ChangeScreen();
		screen.changeScreen(primaryStage, "/boundary/HostClientUI.fxml");
	}
}
