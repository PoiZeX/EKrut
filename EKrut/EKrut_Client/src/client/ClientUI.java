package client;

import javafx.application.Application;

import javafx.stage.Stage;
import utils.AppConfig;
import Store.NavigationStoreController;
import common.CommonFunctions;
import common.PopupTypeEnum;
import controllerGui.HostClientController;
import controllerGui.LoginController;

/**
 * The start class of application
 *
 */
public class ClientUI extends Application {
	/**

	The main method of the application, it sets the system configuration and machine ID based on the command line arguments passed in.
	It also adds a shutdown hook to call the ExitHandler method of NavigationStoreController when the application is closed.
	If no arguments are passed, the system configuration is set to "OL" and the machine ID is ignored.
	The launch method is called to start the application.
	@param args command line arguments passed to the application
	@throws Exception if there is an error while launching the application
	*/
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
				AppConfig.MACHINE_ID = (int) Integer.parseInt(args[2]);

			}
			else
			{
				System.out.println("If you want to work with EK / OL please insert Command as following:\n"
						+ "java -jar EKrut_server.jar arg OL> OR <java -jar EKrut_server.jar arg EK <number>>\n\nBy default work on OL configuration");
				// default
				AppConfig.SYSTEM_CONFIGURATION = "OL";  // and machine id doesn't matter
			}	
		}
		// if args were not inserted - use default
		launch(args);
	}
	/**

	This method starts the application by creating a new HostClientController object and calling its start method, passing the primary stage as a parameter.

	@param primaryStage the primary stage of the application

	@throws Exception if there is any error while starting the application
	*/
	@Override
	public void start(Stage primaryStage) throws Exception {
		HostClientController aFrame = new HostClientController();
		aFrame.start(primaryStage);

	}
}
