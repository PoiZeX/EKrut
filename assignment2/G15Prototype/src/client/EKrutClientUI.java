package client;

import javafx.application.Application;

import javafx.stage.Stage;
import server.EKrutServer;

import java.util.Vector;
import client.EKrutClient;
import common.ChangeScreen;

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
