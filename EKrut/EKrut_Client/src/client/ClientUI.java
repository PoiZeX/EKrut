package client;

import javafx.application.Application;

import javafx.stage.Stage;
import utils.AppConfig;
import Store.NavigationStoreController;
import controllerGui.HostClientController;
import controllerGui.LoginController;

public class ClientUI extends Application {

	public static void main(String[] args) throws Exception {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			NavigationStoreController.ExitHandler(true);
		}));
		if(args.length > 0)
			AppConfig.MACHINE_ID = (int) Integer.parseInt(args[1]);
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		HostClientController aFrame = new HostClientController();
		aFrame.start(primaryStage);

	}
}
