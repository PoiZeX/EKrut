package client;

import javafx.application.Application;

import javafx.stage.Stage;
import utils.AppConfig;
import Store.NavigationStoreController;
import controllerGui.HostClientController;
import controllerGui.LoginController;

/**
 * The start class of application
 *
 */
public class ClientUI extends Application {

	public static void main(String[] args) throws Exception {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			NavigationStoreController.ExitHandler(true);
		}));
		if(args.length > 0)
		{
			// args should be like: <java -jar EKrut_server.jar arg OL> OR <java -jar EKrut_server.jar arg EK <number>> 
			// everything else will be ignored and set to default
				
			if(args[1].equals("OL")) 
			{
				AppConfig.SYSTEM_CONFIGURATION = "OL";

			}
			else if(args[1].equals("EK"))
			{
				AppConfig.SYSTEM_CONFIGURATION = "EK";
				AppConfig.MACHINE_ID = (int) Integer.parseInt(args[1]);

			}
			else
			{
				// default
				AppConfig.SYSTEM_CONFIGURATION = "OL";  // and machine id doesn't matter
			}	
		}
		// if args were not inserted - use default
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		HostClientController aFrame = new HostClientController();
		aFrame.start(primaryStage);

	}
}
