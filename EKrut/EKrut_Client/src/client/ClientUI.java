package client;

import javafx.application.Application;

import javafx.stage.Stage;



import Store.NavigationStoreController;
import controllerGui.HostClientController;
import controllerGui.LoginController;

public class ClientUI extends Application {

	public static void main(String[] args) throws Exception {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			NavigationStoreController.ExitHandler(true);
		}));
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		HostClientController aFrame = new HostClientController();
		aFrame.start(primaryStage);

	}
}
